package com.crypto.treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Domain.PaymentMethod;
import com.crypto.treading.Modal.PaymentOrder;
import com.crypto.treading.Modal.User;
import com.crypto.treading.response.PaymentResponse;
import com.crypto.treading.service.PaymentServiceIml;
import com.crypto.treading.service.UserServiceIml;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

@RestController

public class PaymentController {
	@Autowired
	private UserServiceIml userService;
	
	@Autowired
	private PaymentServiceIml paymentService;
	
	@PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
	public ResponseEntity<PaymentResponse> paymentHandler(@PathVariable("paymentMethod") PaymentMethod paymentMethod, 
			@PathVariable("amount") Long amount,
			@RequestHeader("Authorization") String jwt	
			) throws Exception,RazorpayException,StripeException
	{
		
		User user = userService.findUserProfileByJwt(jwt);
		PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);
		PaymentResponse paymentResponse;
		
		if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
			paymentResponse = paymentService.createRazorPaymentLing(user, amount);
		}else {
			paymentResponse = paymentService.createStripePaymentLing(user, amount, order.getId());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
	}
	
	
	
}
