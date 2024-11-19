package com.crypto.treading.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Domain.VerificationType;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.VerifycationCode;
import com.crypto.treading.Untils.OtpUntil;
import com.crypto.treading.repository.VerificationCodeRepository;
@Service
public class VerificationCodeServiceIml implements VerificationCodeService{
	@Autowired
	private VerificationCodeRepository verificationCodeRepository;
	
	@Override
	public VerifycationCode sendVerificationCode(User user,VerificationType verifycationType) {
		// TODO Auto-generated method stub
		VerifycationCode verifycationCode = new VerifycationCode();
		verifycationCode.setOtp(OtpUntil.generateOtp());
		verifycationCode.setVerificationType(verifycationType);
		verifycationCode.setUser(user);
		
		return verificationCodeRepository.save(verifycationCode);
	}

	@Override
	public VerifycationCode getVerificationCodeById(Long id) throws Exception {
		// TODO Auto-generated method stub
		Optional<VerifycationCode> verificationCode = verificationCodeRepository.findById(id);
		if(verificationCode.isPresent()) {
			return verificationCode.get();
		}
		throw new Exception("Verifycation code not found");
	}

	@Override
	public VerifycationCode getVerificationCodeByUser(Long userId) {
		// TODO Auto-generated method stub
		return verificationCodeRepository.findByUserId(userId);
	}

	@Override
	public void deleteVerificationCodeById(VerifycationCode verifycationCode) {
		// TODO Auto-generated method stub
		 verificationCodeRepository.delete(verifycationCode);
		
	}

}
