package com.crypto.treading.service;

import java.util.List;

import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.WalletTransaction;

public interface WalletTransactionService {
	List<WalletTransaction> getAllTransactionWallet(User user);
}
