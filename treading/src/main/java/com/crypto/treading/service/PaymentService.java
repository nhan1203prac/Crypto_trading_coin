package com.crypto.treading.service;

import java.io.IOException;
import java.net.MalformedURLException;

import com.crypto.treading.Domain.PaymentMethod;
import com.crypto.treading.Modal.PaymentOrder;
import com.crypto.treading.Modal.User;
import com.crypto.treading.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
	PaymentOrder createOrder (User user,Long amount,PaymentMethod paymentMethod);
	PaymentOrder getPaymentOrderId(Long id) throws Exception;
	Boolean proccedpaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException, StripeException, MalformedURLException, IOException;
	PaymentResponse createRazorPaymentLing(User user, Long amount) throws RazorpayException;
	PaymentResponse createStripePaymentLing(User user, Long amount,Long orderId);
}
