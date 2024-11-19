package com.crypto.treading.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Modal.Withdrawal;
import com.crypto.treading.Domain.WithdrawalStatus;
import com.crypto.treading.Modal.User;
import com.crypto.treading.repository.WithdrawalRepository;
@Service
public class WithdrawalServiceIml implements WithdrawalServide{

	@Autowired
	private WithdrawalRepository withdrawalRepository;
	
	@Override
	public Withdrawal requestWithDrawal(Long amount, User user) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setAmount(amount);
		withdrawal.setUser(user);
		withdrawal.setStatus(WithdrawalStatus.PENDING);
		
		
		return withdrawalRepository.save(withdrawal);
	}

	@Override
	public Withdrawal procedWithwithdrawal(Long withdrawalId, boolean accept) throws Exception {
		Optional<Withdrawal> optionWithdrawal = withdrawalRepository.findById(withdrawalId);
		if(optionWithdrawal.isEmpty()) {
			throw new Exception("withdrawal not found");
		}
		
		Withdrawal withdrawal = optionWithdrawal.get();
		
		withdrawal.setDate(LocalDateTime.now());
		if(accept) {
			withdrawal.setStatus(WithdrawalStatus.SUCCESS);
		}else {
			withdrawal.setStatus(WithdrawalStatus.PENDING);
		}
		
		return withdrawalRepository.save(withdrawal);
	}

	@Override
	public List<Withdrawal> getUsersWithdrawalHistory(User user) {
		
		return withdrawalRepository.findByUserId(user.getId());
		 
	}

	@Override
	public List<Withdrawal> getAllWithdrawalRequest() {
		
		return withdrawalRepository.findAll();
	}

}
