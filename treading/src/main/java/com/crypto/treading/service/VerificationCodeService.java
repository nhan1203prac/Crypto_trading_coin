package com.crypto.treading.service;

import com.crypto.treading.Domain.VerificationType;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.VerifycationCode;

public interface VerificationCodeService {
	VerifycationCode sendVerificationCode(User user,VerificationType verifycationType);
	VerifycationCode getVerificationCodeById(Long id) throws Exception;
	VerifycationCode getVerificationCodeByUser(Long userId);
	void deleteVerificationCodeById(VerifycationCode verifycationCode);
}
