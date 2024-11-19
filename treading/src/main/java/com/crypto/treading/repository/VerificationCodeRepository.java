package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.VerifycationCode;

public interface VerificationCodeRepository extends JpaRepository<VerifycationCode, Long>{
	public VerifycationCode findByUserId(Long userId);
}
