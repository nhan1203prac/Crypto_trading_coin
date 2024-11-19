package com.crypto.treading.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Modal.TwoFactorOtp;
import com.crypto.treading.Modal.User;
import com.crypto.treading.repository.TwoFactorOtpRepository;

@Service
public class TwoFactorOtpServiceIml implements TwoFactorOtpService{

	@Autowired
	private TwoFactorOtpRepository twoFactorOtpRepository;
	
	@Override
	public TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt) {
		// TODO Auto-generated method stub
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		
		TwoFactorOtp twoFactor = TwoFactorOtp.builder()
				.id(id)
				.otp(otp)
				.jwt(jwt)
				.user(user)
				.build();
		
		return twoFactorOtpRepository.save(twoFactor);
	}

	@Override
	public TwoFactorOtp findByUser(Long userId) {
		// TODO Auto-generated method stub
		
		
		return twoFactorOtpRepository.findByUserId(userId);
	}

	@Override
	public TwoFactorOtp findById(String id) {
		// TODO Auto-generated method stub
		TwoFactorOtp twoFactorOtp = twoFactorOtpRepository.findById(id).orElse(null);
		return twoFactorOtp;
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
		// TODO Auto-generated method stub
		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
		// TODO Auto-generated method stub
		twoFactorOtpRepository.delete(twoFactorOtp);
	}

}
