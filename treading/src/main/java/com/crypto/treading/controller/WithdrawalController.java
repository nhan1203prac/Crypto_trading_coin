package com.crypto.treading.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Modal.Withdrawal;
import com.crypto.treading.repository.WalletTransactionRepository;
import com.crypto.treading.Domain.WalletTransactionType;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.Wallet;
import com.crypto.treading.Modal.WalletTransaction;
import com.crypto.treading.service.UserServiceIml;
import com.crypto.treading.service.WalletServiceIml;
import com.crypto.treading.service.WithdrawalServiceIml;


@RestController
//@RequestMapping("/api/withdrawal")
public class WithdrawalController {
	@Autowired
	private WithdrawalServiceIml withdrawalServideIml;
	@Autowired
	private WalletServiceIml walletServiceIml;
	@Autowired
	private UserServiceIml userServiceIml;
	@Autowired
	private WalletTransactionRepository walletTransactionRepository;
	
	@PostMapping("/api/withdrawal/{amount}")
	public ResponseEntity<?> withdrawalRequest(@PathVariable("amount") Long amount, @RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		Wallet wallet = walletServiceIml.getUserWaller(user);
		Withdrawal withdrawal = withdrawalServideIml.requestWithDrawal(amount, user);
		walletServiceIml.addBalance(wallet, -amount);
		
		return ResponseEntity.status(HttpStatus.OK).body(withdrawal);
		
	}
	
	@PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
	public ResponseEntity<?> proceedWithdrawal(@PathVariable("id") Long id, @PathVariable("accept") boolean accept,@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		Withdrawal withdrawal = withdrawalServideIml.procedWithwithdrawal(id, accept);
		
		Wallet wallet = walletServiceIml.getUserWaller(user);
		if(!accept) {
			walletServiceIml.addBalance(wallet, withdrawal.getAmount());
		}
		else {
			WalletTransaction req = new WalletTransaction();
			req.setDatel(LocalDate.now());
			req.setWalletTransactionType(WalletTransactionType.WITHDRAWAL);
			req.setWallet(wallet);
			req.setAmount(withdrawal.getAmount());
			req.setPurpose("DEPOSIT");
			walletTransactionRepository.save(req);
		}
		return ResponseEntity.status(HttpStatus.OK).body(withdrawal);
		
	}
	
	
	@GetMapping("/api/withdrawal")
	public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		List<Withdrawal> listWithdrawal = withdrawalServideIml.getUsersWithdrawalHistory(user);
		return ResponseEntity.status(HttpStatus.OK).body(listWithdrawal);
		
	}
	@GetMapping("/api/admin/withdrawal")
	public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		
		List<Withdrawal> withdrawal = withdrawalServideIml.getAllWithdrawalRequest();
		return ResponseEntity.status(HttpStatus.OK).body(withdrawal);
	}
	
}
	