package com.crypto.treading.service;

import com.crypto.treading.Modal.PaymentDetails;
import com.crypto.treading.Modal.User;

public interface PaymentDetailService {
	public PaymentDetails addPaymentDetails(String accountNumber,String accountHolderName,String ifsc,String bankName,User user);
	public PaymentDetails getUserPaymentDetails(User user);
}
