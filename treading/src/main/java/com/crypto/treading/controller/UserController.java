package com.crypto.treading.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Domain.VerificationType;
import com.crypto.treading.Modal.ForgotPasswordToken;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.VerifycationCode;
import com.crypto.treading.Untils.OtpUntil;
import com.crypto.treading.repository.ForgotPasswordTokenRepository;
import com.crypto.treading.repository.UserRepository;
import com.crypto.treading.request.ForgotPasswordTokenRequest;
import com.crypto.treading.request.ResetPasswordRequest;
import com.crypto.treading.response.ApiResponse;
import com.crypto.treading.response.AuthResponse;
import com.crypto.treading.service.EmailService;
import com.crypto.treading.service.ForgotPasswordTokenServiceIml;
import com.crypto.treading.service.UserServiceIml;
import com.crypto.treading.service.VerificationCodeService;
import com.crypto.treading.service.VerificationCodeServiceIml;

import jakarta.mail.MessagingException;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserServiceIml userServiceIml;
	@Autowired
	private EmailService emailService;
	@Autowired
	private VerificationCodeServiceIml verificationCodeService;
	@Autowired
	private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
	@Autowired
	private ForgotPasswordTokenServiceIml forgotPasswordTokenService;
	
	@GetMapping("/api/users/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@PostMapping("/api/user/verification/{verificationType}/send-otp")
	public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt, 
			@PathVariable("verificationType") VerificationType verificationType) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		VerifycationCode verifycationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
		if(verifycationCode==null) {
			verifycationCode = verificationCodeService.sendVerificationCode(user, verificationType);
			
		}
		if(verificationType.equals(VerificationType.EMAIL)) {
			emailService.sendVerifycationOtpEmail(user.getEmail(), verifycationCode.getOtp());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("Verification otp sent successfully");
		 
	}
	@PatchMapping("/api/user/enable-two-factor/verify-otp/{otp}")
	public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable("otp") String otp, @RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		VerifycationCode verifycationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
		String sendTo = verifycationCode.getVerificationType().equals(VerificationType.EMAIL)?verifycationCode.getEmail():verifycationCode.getMobile();
		boolean isVerified = verifycationCode.getOtp().equals(otp);
		if(isVerified) {
			User updateUser = userServiceIml.enableTwoFactorAuthentication(verifycationCode.getVerificationType(), sendTo, user);
			verificationCodeService.deleteVerificationCodeById(verifycationCode);
			return ResponseEntity.status(HttpStatus.OK).body(updateUser);
		}
		throw new Exception("Wrong otp"); 
	}
	
	@PostMapping("/api/user/reset-password/send-otp")
	public ResponseEntity<AuthResponse> sendForgotPasswordOtp(@RequestBody ForgotPasswordTokenRequest req) throws Exception{
		String otp = OtpUntil.generateOtp();
		User user = userRepository.findByEmail(req.getSendTo());
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		
		ForgotPasswordToken token = forgotPasswordTokenRepository.findByUserId(user.getId());
		if(token ==null) {
			token = forgotPasswordTokenService.createForgotTokenToken(id, user, otp, req.getVerificationType(), req.getSendTo());
		}
		if(req.getVerificationType().equals(VerificationType.EMAIL)) {
			emailService.sendVerifycationOtpEmail(req.getSendTo(), token.getOtp());
		}
		AuthResponse response = AuthResponse.builder()
				.message("Password reset otp sent successfully")
				.session(token.getId())
				.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/api/user/reset-password/verify-otp")
	public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest,@RequestParam String id) throws Exception{
		ForgotPasswordToken token = forgotPasswordTokenService.findById(id);
		boolean verified = token.getOtp().equals(resetPasswordRequest.getOtp());
		if(verified) {
			userServiceIml.updatePassword(token.getUser(), resetPasswordRequest.getPassword());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.builder().message("Password updated successfully").build());
			
		}
		throw new Exception("wrong otp");
	}
	
	@GetMapping("/api/userss")
	ResponseEntity<List<User>> getUser(){
		return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
	}
	
}
