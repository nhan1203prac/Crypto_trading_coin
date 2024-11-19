package com.crypto.treading.service;

import java.util.List;

import com.crypto.treading.Domain.OrderType;
import com.crypto.treading.Modal.Coin;
import com.crypto.treading.Modal.Order;
import com.crypto.treading.Modal.OrderItem;
import com.crypto.treading.Modal.User;

public interface OrderService {
	Order createOrder(User user, OrderItem orderItem,OrderType orderType);
	Order getOrderById(Long orderId) throws Exception;
	List<Order> getAllOrdersOfUser(Long userId,OrderType orderType,String assetSymbol);
	Order processOrder(Coin coin,double quantity,OrderType orderType,User user) throws Exception;
}
