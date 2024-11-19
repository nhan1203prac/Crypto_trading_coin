package com.crypto.treading.service;

import java.util.List;

import com.crypto.treading.Modal.Withdrawal;
import com.crypto.treading.Modal.User;

public interface WithdrawalServide {
	Withdrawal requestWithDrawal(Long amount, User user);
	Withdrawal procedWithwithdrawal(Long withdrawalId, boolean accept) throws Exception;
	List<Withdrawal> getUsersWithdrawalHistory(User user);
	
	List<Withdrawal> getAllWithdrawalRequest();
}
