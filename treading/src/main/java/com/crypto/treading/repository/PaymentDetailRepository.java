package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.PaymentDetails;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetails, Long> {
	PaymentDetails findByUserId(Long userId);
	
}
