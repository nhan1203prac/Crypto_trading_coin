package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.TwoFactorOtp;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOtp, String>{
	TwoFactorOtp findByUserId(Long userId);
}
