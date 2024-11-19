package com.crypto.treading.service;

import com.crypto.treading.Domain.VerificationType;
import com.crypto.treading.Modal.ForgotPasswordToken;
import com.crypto.treading.Modal.User;

public interface ForgotPasswordTokenService {
	public ForgotPasswordToken createForgotTokenToken(String id, User user, String otp, VerificationType verificationType, String sendTo);
	public ForgotPasswordToken findByUser(Long userId);
	public ForgotPasswordToken findById(String id);
	public void deleteToken(ForgotPasswordToken  forgotPasswordToken);
}
