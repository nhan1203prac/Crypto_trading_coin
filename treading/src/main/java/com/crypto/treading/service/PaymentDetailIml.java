package com.crypto.treading.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Modal.PaymentDetails;

import com.crypto.treading.Modal.User;
import com.crypto.treading.repository.PaymentDetailRepository;
@Service
public class PaymentDetailIml implements PaymentDetailService{
	@Autowired
	private PaymentDetailRepository paymentDetailRepository;
	@Override
	public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc,String bankName, User user) {
		PaymentDetails paymentDetails = new PaymentDetails();
		paymentDetails.setAccountHolderName(accountHolderName);
		paymentDetails.setAccountNumber(accountNumber);
		paymentDetails.setBankName(bankName);
		paymentDetails.setIfsc(ifsc);
		paymentDetails.setUser(user);
		return paymentDetailRepository.save(paymentDetails);
	}

	@Override
	public PaymentDetails getUserPaymentDetails(User user) {
		
		return paymentDetailRepository.findByUserId(user.getId());
	}
	
}
