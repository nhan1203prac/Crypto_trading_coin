package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.Wallet;

public interface WallerRepository extends JpaRepository<Wallet, Long> {
	Wallet findByUserId(Long userId);
}
