package com.crypto.treading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Domain.OrderType;
import com.crypto.treading.Modal.Coin;
import com.crypto.treading.Modal.Order;
import com.crypto.treading.Modal.User;
import com.crypto.treading.request.createOrderRequest;
import com.crypto.treading.service.CoinService;
import com.crypto.treading.service.CoinServiceIml;
import com.crypto.treading.service.OrderService;
import com.crypto.treading.service.OrderServiceIml;
import com.crypto.treading.service.UserService;
import com.crypto.treading.service.UserServiceIml;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
	@Autowired
	private OrderServiceIml orderService;
	@Autowired
	private UserServiceIml userService;
	@Autowired
	private CoinServiceIml coinService;
	
//	@Autowired
//	private WalletTransactionService walletTransactionService;
	 
	@PostMapping("/pay")
	public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt, @RequestBody createOrderRequest req) throws Exception{
		User user = userService.findUserProfileByJwt(jwt);
		Coin coin = coinService.findById(req.getCoinId());
		Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);
		return ResponseEntity.status(HttpStatus.OK).body(order);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt, @PathVariable("orderId") Long orderId) throws Exception{
		User user = userService.findUserProfileByJwt(jwt);
		Order order = orderService.getOrderById(orderId);
		if(order.getUser().getId().equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.OK).body(order);
		}else {
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			throw new Exception("you don't have access");
		}
	}
	
	@GetMapping()
	public ResponseEntity<List<Order>> getAllOrderByUserId(@RequestHeader("Authorization") String jwt,
	@RequestParam(value = "orderType",required = false) OrderType orderType, @RequestParam(value="asset_symbol",required = false) String asset_symbol) throws Exception{
		Long userId = userService.findUserProfileByJwt(jwt).getId();

		List<Order> userOrders = orderService.getAllOrdersOfUser(userId, orderType!=null?orderType:null, asset_symbol);
		return ResponseEntity.ok(userOrders);
	}
}
