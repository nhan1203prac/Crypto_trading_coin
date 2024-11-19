package com.crypto.treading.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Domain.OrderType;
import com.crypto.treading.Domain.WalletTransactionType;
import com.crypto.treading.Modal.Order;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.Wallet;
import com.crypto.treading.Modal.WalletTransaction;
import com.crypto.treading.repository.WallerRepository;
import com.crypto.treading.repository.WalletTransactionRepository;

@Service
public class WalletServiceIml implements WallerService{
	@Autowired
	private WallerRepository walletRepository;

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;
	@Override
	public Wallet getUserWaller(User user) {
		Wallet wallet = walletRepository.findByUserId(user.getId());
		if(wallet==null) {
			wallet = new Wallet();
			wallet.setUser(user);
			wallet.setBalance(BigDecimal.valueOf(0));
			walletRepository.save(wallet);
		}
		if(wallet.getBalance()==null)
			wallet.setBalance(BigDecimal.valueOf(0));
		
		return wallet;
	}

	@Override
	public Wallet addBalance(Wallet wallet, Long money) {
		BigDecimal balance = wallet.getBalance();
		BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
		wallet.setBalance(newBalance);
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet findWalletById(Long id) throws Exception {
		Optional<Wallet> wallet = walletRepository.findById(id);
		if(wallet.isPresent()) {
			
			return wallet.get();
		}
		throw new Exception("wallet not found");
	}

	@Override
	public Wallet payOrderPayment(Order order, User user) throws Exception {
		Wallet wallet = getUserWaller(user);
		if(order.getOrderType().equals(OrderType.BUY)) {
			if(wallet.getBalance().compareTo(order.getPrice())<0) {
				throw new Exception("Insufficident funds for this transaction");
			}
			BigDecimal newbalance = wallet.getBalance().subtract(order.getPrice());
			wallet.setBalance(newbalance);
		}
		else {
			BigDecimal newbalance = wallet.getBalance().add(order.getPrice());
			wallet.setBalance(newbalance);
		}
		walletRepository.save(wallet);
		return wallet;
	}

	@Override
	public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
		Wallet walletSender = getUserWaller(sender);
		if(walletSender.getBalance().compareTo(BigDecimal.valueOf(amount))<0) {
			throw new Exception("Insufficident balance...");
		}
		BigDecimal senderBalance = walletSender.getBalance().subtract(BigDecimal.valueOf(amount));
		walletSender.setBalance(senderBalance);
		walletRepository.save(walletSender);
		
//		nếu receiver wallet .getbalance is null
		if(receiverWallet.getBalance()==null)
			receiverWallet.setBalance(BigDecimal.valueOf(0));
		
		receiverWallet.setBalance(receiverWallet.getBalance().add(BigDecimal.valueOf(amount)));
		WalletTransaction walletTran = new WalletTransaction();
		walletTran.setAmount(amount);
		walletTran.setDatel(LocalDate.now());
		walletTran.setWalletTransactionType(WalletTransactionType.WALLET_TRANSFER);
		walletTran.setTransferId(walletSender.getId());
		walletTran.setWallet(receiverWallet);
		walletTran.setPurpose("Receiver money");
		walletTransactionRepository.save(walletTran);
		walletRepository.save(receiverWallet);
		
		
		return walletSender;
	}

	
	
	
}
