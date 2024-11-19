package com.crypto.treading.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.Wallet;
import com.crypto.treading.Modal.WalletTransaction;
import com.crypto.treading.repository.WalletTransactionRepository;
@Service
public class WalletTransactionServiceIml implements WalletTransactionService{

	@Autowired
	private WalletServiceIml walletServiceIml;
	@Autowired
	private WalletTransactionRepository walletTransactionRepository;
	@Override
	public List<WalletTransaction> getAllTransactionWallet(User user) {
		Wallet wallet = walletServiceIml.getUserWaller(user);
		
		return walletTransactionRepository.findByWalletId(wallet.getId());
	}

}
