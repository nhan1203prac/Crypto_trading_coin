package com.crypto.treading.service;

import com.crypto.treading.Modal.TwoFactorOtp;
import com.crypto.treading.Modal.User;

public interface TwoFactorOtpService {

	TwoFactorOtp createTwoFactorOtp(User user,String otp,String jwt);
	
	TwoFactorOtp findByUser(Long userId);
	TwoFactorOtp findById(String id);
	
	boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp);
	
	void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}
