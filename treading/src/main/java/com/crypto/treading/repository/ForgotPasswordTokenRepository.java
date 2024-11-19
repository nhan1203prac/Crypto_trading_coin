package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.ForgotPasswordToken;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, String>{
	ForgotPasswordToken findByUserId(Long userId);
}
