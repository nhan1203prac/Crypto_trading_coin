package com.crypto.treading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Domain.VerificationType;
import com.crypto.treading.Modal.ForgotPasswordToken;
import com.crypto.treading.Modal.User;
import com.crypto.treading.repository.ForgotPasswordTokenRepository;

@Service
public class ForgotPasswordTokenServiceIml implements ForgotPasswordTokenService {
	@Autowired 
	private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
	@Override
	public ForgotPasswordToken createForgotTokenToken(String id, User user, String otp,
			VerificationType verificationType, String sendTo) {
		ForgotPasswordToken token = ForgotPasswordToken.builder()
				.id(id)
				.user(user)
				.otp(otp)
				.verificationType(verificationType)
				.sendTo(sendTo)
				.build();
		return forgotPasswordTokenRepository.save(token);
	}

	@Override
	public ForgotPasswordToken findByUser(Long userId) {
		
		return forgotPasswordTokenRepository.findByUserId(userId);
	}

	@Override
	public ForgotPasswordToken findById(String id) {
		
		return forgotPasswordTokenRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteToken(ForgotPasswordToken forgotPasswordToken) {
		forgotPasswordTokenRepository.delete(forgotPasswordToken);
		
	}

}
