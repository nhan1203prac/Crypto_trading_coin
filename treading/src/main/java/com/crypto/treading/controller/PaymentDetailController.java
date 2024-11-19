package com.crypto.treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Modal.PaymentDetails;
import com.crypto.treading.Modal.User;
import com.crypto.treading.service.PaymentDetailIml;
import com.crypto.treading.service.UserServiceIml;

@RestController
@RequestMapping("/api")
public class PaymentDetailController {
	@Autowired
	private UserServiceIml userServiceIml;
	
	@Autowired
	private PaymentDetailIml paymentDetailIml;
	
	@PostMapping("/payment-details")
	public ResponseEntity<PaymentDetails> addPaymentDetail(@RequestBody PaymentDetails paymentDetailRequest,@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		PaymentDetails paymentDetails = paymentDetailIml.addPaymentDetails(paymentDetailRequest.getAccountNumber(),
				paymentDetailRequest.getAccountHolderName(), 
				paymentDetailRequest.getIfsc(), 
				paymentDetailRequest.getBankName(), 
				user);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentDetails);
	}
	
	@GetMapping("/payment-details")
	public ResponseEntity<PaymentDetails> getUserPaymentDetail(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		PaymentDetails paymentDetails = paymentDetailIml.getUserPaymentDetails(user);
		
		return ResponseEntity.status(HttpStatus.OK).body(paymentDetails);
		
	}
}
