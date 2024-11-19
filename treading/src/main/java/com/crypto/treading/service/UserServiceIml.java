package com.crypto.treading.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Domain.VerificationType;
import com.crypto.treading.Modal.TwoFactorAuth;
import com.crypto.treading.Modal.User;
import com.crypto.treading.config.JwtProvider;
import com.crypto.treading.repository.UserRepository;
@Service
public class UserServiceIml implements UserService{
	@Autowired
	private UserRepository userRepository;
	@Override
	public User findUserProfileByJwt(String jwt) throws Exception {
		// TODO Auto-generated method stub
		String email = JwtProvider.getEmailFromToken(jwt);
		User user = userRepository.findByEmail(email);
		if(user==null) {
			throw new Exception("User not found");
		}
		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if(user==null) {
			throw new Exception("User not found");
		}
		return user;
	}

	@Override
	public User findUserById(Long userId) throws Exception {
		Optional<User> optionUser  = userRepository.findById(userId);
		if(optionUser.isEmpty()) {
			throw new Exception("User not found");
		}
		return optionUser.get();
	}

	@Override
	public User enableTwoFactorAuthentication(VerificationType verifycationType,String sendTo,User user) {
		TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
		twoFactorAuth.setEnabled(true);
		twoFactorAuth.setSendTo(verifycationType);
		user.setTwoFactorAuth(twoFactorAuth);
		return userRepository.save(user);
	}

	@Override
	public User updatePassword(User user, String newPassword) {
		user.setPassword(newPassword);
		
		return userRepository.save(user);
	}

}
