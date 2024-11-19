package com.crypto.treading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.WalletTransaction;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long > {
	List<WalletTransaction> findByWalletId(Long id);
}
