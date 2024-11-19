package com.crypto.treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Modal.TwoFactorOtp;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.Wallet;
import com.crypto.treading.Untils.OtpUntil;
import com.crypto.treading.config.JwtProvider;
import com.crypto.treading.repository.TwoFactorOtpRepository;
import com.crypto.treading.repository.UserRepository;
import com.crypto.treading.response.AuthResponse;
import com.crypto.treading.service.CustomUserDetailService;
import com.crypto.treading.service.EmailService;
import com.crypto.treading.service.TwoFactorOtpService;
import com.crypto.treading.service.TwoFactorOtpServiceIml;
import com.crypto.treading.service.WalletServiceIml;
import com.crypto.treading.service.WatchListServiceIml;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomUserDetailService customUserDetailService;
	@Autowired
	private TwoFactorOtpServiceIml twoFactorOtpService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private WalletServiceIml walletServiceIml;
	
	@Autowired
	private WatchListServiceIml watchListService;
	@PostMapping("signup")
	
	ResponseEntity<AuthResponse> Register (@RequestBody User user) throws Exception{
		User isEmailExisted = userRepository.findByEmail(user.getEmail());
		if(isEmailExisted!=null) {
			throw new Exception("Email is used in another account");
		}
		User newUser = new User();
		newUser.setFullName(user.getFullName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		User savedUser = userRepository.save(newUser);
		
		watchListService.createWatchList(savedUser);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		String jwt = JwtProvider.generateToken(auth);
		return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
				.jwt(jwt)
				.message("Register success")
				.status(true)
				.build());
	}
	
@PostMapping("signin")
	
	ResponseEntity<AuthResponse> Login (@RequestBody User user) throws Exception{
		String username = user.getEmail();
		String password = user.getPassword();
		
		Authentication auth = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		String jwt = JwtProvider.generateToken(auth);
		User authUser = userRepository.findByEmail(username);
		if(authUser.getTwoFactorAuth().isEnabled()) {
			String otp = OtpUntil.generateOtp();
			AuthResponse res = AuthResponse.builder()
					.message("Two Factor auth is enabled")
					.isTwoFatorAuthEnabled(true)
					.jwt(jwt)
					.status(true)
					.build();
			
			TwoFactorOtp OldtwoFactorOtp = twoFactorOtpService.findByUser(authUser.getId());
			if(OldtwoFactorOtp!=null) {
				twoFactorOtpService.deleteTwoFactorOtp(OldtwoFactorOtp);
			}
			
			TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser, otp, jwt);
			emailService.sendVerifycationOtpEmail(username, otp);
			res.setSession(newTwoFactorOtp.getId());
			
//			create wallet
			Wallet wallet = walletServiceIml.getUserWaller(authUser);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
				.jwt(jwt)
				.message("Login success")
				.status(true)
				.build());
	}

	public Authentication authenticate(String username, String password) {
		UserDetails userDetail = customUserDetailService.loadUserByUsername(username);
		if(userDetail==null) {
			throw new BadCredentialsException("invalid username");
		}
		if(!password.equals(userDetail.getPassword())) {
			throw new BadCredentialsException("invalid password");
		}
		return new UsernamePasswordAuthenticationToken(userDetail,password,userDetail.getAuthorities());
	}
	
	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse> verifySigningOtp(@PathVariable("otp") String otp, @RequestParam String id) throws Exception{
		TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);
		if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp, otp)) {
			AuthResponse res = AuthResponse.builder()
					.message("Two factor authentication verified")
					.jwt(twoFactorOtp.getJwt())
					.isTwoFatorAuthEnabled(true)
					.build();
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
		throw new Exception("Invalid otp");
		
	}
	
}
