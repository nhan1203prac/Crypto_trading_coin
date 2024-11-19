package com.crypto.treading.controller;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Domain.WalletTransactionType;
import com.crypto.treading.Modal.Order;
import com.crypto.treading.Modal.PaymentOrder;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.Wallet;
import com.crypto.treading.Modal.WalletTransaction;
import com.crypto.treading.repository.UserRepository;
import com.crypto.treading.repository.WalletTransactionRepository;
import com.crypto.treading.response.PaymentResponse;
import com.crypto.treading.service.OrderService;
import com.crypto.treading.service.OrderServiceIml;
import com.crypto.treading.service.PaymentServiceIml;
import com.crypto.treading.service.UserService;
import com.crypto.treading.service.UserServiceIml;
import com.crypto.treading.service.WalletServiceIml;

	



@RestController
@RequestMapping("/api/wallet")
public class WalletController {
	@Autowired
	private WalletServiceIml walletService;
	
	@Autowired
	private UserServiceIml userService;
	@Autowired
	private OrderServiceIml orderService;
	
	@Autowired
	private PaymentServiceIml paymentService;
	
	@Autowired
	private WalletTransactionRepository transactionRepository;
	@GetMapping
	public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserProfileByJwt(jwt);
		Wallet wallet = walletService.getUserWaller(user);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(wallet);
		
	}
	
	@PutMapping("/{walletId}/transfer")
	public ResponseEntity<Wallet> walletToWalletTransfer(
			@RequestHeader("Authorization") String jwt,
			@PathVariable("walletId") Long walletId,
			@RequestBody WalletTransaction req
			) throws Exception
	{
		User sender = userService.findUserProfileByJwt(jwt);
		Wallet receiverWallet = walletService.findWalletById(walletId);
		Wallet wallet = walletService.walletToWalletTransfer(sender, receiverWallet, req.getAmount());
		
		req.setDatel(LocalDate.now());
		req.setWalletTransactionType(WalletTransactionType.WALLET_TRANSFER);
		req.setWallet(wallet);
		req.setTransferId(walletId);
		transactionRepository.save(req);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(wallet);
	}
	
	@PutMapping("/order/{orderId}/pay")
	public ResponseEntity<Wallet> payOrderPayment(
			@RequestHeader("Authorization") String jwt,
			@PathVariable("orderId") Long orderId
			
			) throws Exception
	{
		User sender = userService.findUserProfileByJwt(jwt);
		Order order = orderService.getOrderById(orderId);
		Wallet wallet = walletService.payOrderPayment(order, sender);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(wallet);
	}
	
	@PutMapping("/deposit")
	public ResponseEntity<Wallet> addBalanceToWallet(
			@RequestHeader("Authorization") String jwt,
			@RequestParam(name="order_id") Long orderId,
			@RequestParam(name="payment_id") String paymentId
			
			) throws Exception
	{
		User sender = userService.findUserProfileByJwt(jwt);
		
		Wallet wallet =walletService.getUserWaller(sender);
		PaymentOrder order = paymentService.getPaymentOrderId(orderId);
		Boolean status = paymentService.proccedpaymentOrder(order, paymentId);
		
		if(wallet.getBalance()==null)
			wallet.setBalance(BigDecimal.valueOf(0));
		System.out.println("----------------" + order.getAmount());
		if(status) {
			wallet = walletService.addBalance(wallet, order.getAmount());
		}
		
		WalletTransaction req = new WalletTransaction();
		req.setAmount(order.getAmount());
		req.setDatel(LocalDate.now());
		req.setPurpose("Deposit");
		req.setWallet(wallet);
		req.setWalletTransactionType(WalletTransactionType.ADD_MONEY);
		transactionRepository.save(req);
		
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(wallet);
	}
}


