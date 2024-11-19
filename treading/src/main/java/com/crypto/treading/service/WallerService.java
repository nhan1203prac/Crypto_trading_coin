package com.crypto.treading.service;

import java.util.List;

import com.crypto.treading.Modal.Order;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.Wallet;

public interface WallerService {
	Wallet getUserWaller(User user);
	Wallet addBalance(Wallet wallet, Long money);
	Wallet findWalletById(Long id) throws Exception;
	Wallet walletToWalletTransfer(User sender,Wallet receiverWallet, Long amount) throws Exception;
	Wallet payOrderPayment(Order order, User user) throws Exception;
	
}
