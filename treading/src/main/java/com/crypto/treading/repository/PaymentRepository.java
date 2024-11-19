package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.PaymentOrder;

public interface PaymentRepository extends JpaRepository<PaymentOrder, Long>{

}
