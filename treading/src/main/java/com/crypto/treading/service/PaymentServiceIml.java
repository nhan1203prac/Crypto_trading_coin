package com.crypto.treading.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.crypto.treading.Domain.PaymentMethod;
import com.crypto.treading.Domain.PaymentOrderStatus;
import com.crypto.treading.Modal.PaymentOrder;
import com.crypto.treading.Modal.User;
import com.crypto.treading.repository.PaymentRepository;
import com.crypto.treading.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
@Service
public class PaymentServiceIml implements PaymentService{
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Value("${stripe.api.key}")
	private String stripeSecretKey;
	
	@Value("${razorpay.api.key}")
	private String apiKey;
	
	@Value("${razorpay.api.secret}")
	private String apiSecretKey;
	
	@Override
	public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setAmount(amount);
		paymentOrder.setPaymentMethod(paymentMethod);
		paymentOrder.setUser(user);
		paymentOrder.setStatus(PaymentOrderStatus.PENDING);
		return paymentRepository.save(paymentOrder);
	}

	@Override
	public PaymentOrder getPaymentOrderId(Long id) throws Exception {
		
		return paymentRepository.findById(id).orElseThrow(()->new Exception("payment order not found"));
	}

	@Override
	public Boolean proccedpaymentOrder(PaymentOrder paymentOrder, String paymentId) throws StripeException {
		if(paymentOrder.getStatus()==null)
			paymentOrder.setStatus(PaymentOrderStatus.PENDING);
	    if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
	        if(paymentOrder.getPaymentMethod().equals(PaymentMethod.STRIPE)) {
	          
	            Stripe.apiKey =stripeSecretKey;

	            
//	            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);
	            Session retrievedSession  = Session.retrieve(paymentId);
	            
	            String status = retrievedSession.getStatus();
	            Long amount = retrievedSession.getAmountTotal();
	            
	            
	            System.out.println("--------status" + status);
//	            Integer amount = paymentIntent.getAmount().intValue();
//	            String status = paymentIntent.getStatus();

	            
	            if(status.equals("complete")) {
	                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
	                System.out.println("inside success" + paymentOrder.getStatus().toString());
	                paymentRepository.save(paymentOrder);
	                return true;
	            } else {
	                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
	                paymentRepository.save(paymentOrder);
	                return false;
	            }
	        }

	        paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
	        paymentRepository.save(paymentOrder);
	        return true;
	    }
	    return false;
	}

	@Override
	public PaymentResponse createRazorPaymentLing(User user, Long amount) throws RazorpayException {
		Long Amount = amount * 100;
		try {
			RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecretKey);
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", Amount);
			paymentLinkRequest.put("currency", "USD");
			
			JSONObject customer = new JSONObject();
			customer.put("name", user.getFullName());
			
			customer.put("email", user.getEmail());
			
			paymentLinkRequest.put("customer", customer);
			
			JSONObject notify = new JSONObject();
			notify.put("email", true);
			paymentLinkRequest.put("notify", notify);
			
			paymentLinkRequest.put("reminder_enable", true);
			
			paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet");
			paymentLinkRequest.put("callback_method", "get");
			
			PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkId = payment.get("id");
			String paymentLinkUrl = payment.get("short_url");
			
			PaymentResponse res = new PaymentResponse();
			res.setPayment_url(paymentLinkUrl);
		
			return res;
		}catch(RazorpayException e){
			System.out.println("Error creating payment link: "+e.getMessage());
			throw new RazorpayException(e.getMessage());
		}
	
	}

	@Override
	public PaymentResponse createStripePaymentLing(User user, Long amount, Long orderId) {
		 Stripe.apiKey="sk_test_51P5IjLP1PtyodjX4QVY760xOBAmkJBWxzOWLQr4dujuFVMxWbMZkHGMZUkDwg3k4F5hiR7NCVt1SdBRiK6ix7ZB300PZx6CwkS";

		    // Tạo các tham số để tạo phiên thanh toán
		    SessionCreateParams params = SessionCreateParams.builder()
//		        .setPaymentMethodTypes(Collections.singletonList("card")) // Phương thức thanh toán
		        .setMode(SessionCreateParams.Mode.PAYMENT)   
		        .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId + "&paymentId={CHECKOUT_SESSION_ID}") 
		        .setCancelUrl("http://localhost:5173/payment/cancel")              
		        .addLineItem(
		            SessionCreateParams.LineItem.builder()
		                .setQuantity(1L)                 
		                .setPriceData(
		                    SessionCreateParams.LineItem.PriceData.builder()  
		                        .setCurrency("usd")                           
		                        .setUnitAmount(amount * 100)   
		                        
		                        .setProductData(
		                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
		                                .setName("Top up wallet") 
		                                .build()
		                        )
		                        .build()
		                )
		                .build()
		        )
		        .build();

		    try {
		      
		        Session session = Session.create(params);
		        System.out.println("Session: " + session);
		        
		       
		        
		        String paymentIntentId = session.getId();
		       
		        
		        PaymentResponse res = new PaymentResponse();
		        res.setPaymentIntentId(paymentIntentId);
		       
		        res.setPayment_url(session.getUrl());
		        return res;
		    } catch (Exception e) {
		        e.printStackTrace();
		        return null;
		    }
	}

}
