package com.crypto.treading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.Withdrawal;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long>{
	List<Withdrawal> findByUserId(Long userId);

}
