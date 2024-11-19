package com.crypto.treading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.WalletTransaction;
import com.crypto.treading.repository.WalletTransactionRepository;
import com.crypto.treading.service.UserServiceIml;
import com.crypto.treading.service.WalletTransactionServiceIml;

@RestController
public class WalletTransactionController {
	@Autowired
	private WalletTransactionRepository walletTransactionRepository;
	@Autowired
	private WalletTransactionServiceIml transactionServiceIml;
	@Autowired
	private UserServiceIml userServiceIml;
	
	@GetMapping("/api/transaction")
	ResponseEntity<List<WalletTransaction>> getAllTransactionWallet(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		return ResponseEntity.status(HttpStatus.OK).body(transactionServiceIml.getAllTransactionWallet(user));
	}
}
