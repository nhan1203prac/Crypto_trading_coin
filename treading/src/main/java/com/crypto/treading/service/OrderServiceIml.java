package com.crypto.treading.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Domain.OrderStatus;
import com.crypto.treading.Domain.OrderType;
import com.crypto.treading.Modal.Asset;
import com.crypto.treading.Modal.Coin;
import com.crypto.treading.Modal.Order;
import com.crypto.treading.Modal.OrderItem;
import com.crypto.treading.Modal.User;

import com.crypto.treading.repository.OrderItemRepository;
import com.crypto.treading.repository.OrderRepository;

import jakarta.transaction.Transactional;
@Service
public class OrderServiceIml implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private WalletServiceIml walletService;
	@Autowired
	private AssetServiceIml assetService;
	
	@Override
	public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
		double price = orderItem.getQuantity()*orderItem.getCoin().getCurrentPrice();
		
		Order order = new Order();
		order.setPrice(BigDecimal.valueOf(price));
		order.setOrderItem(orderItem);
		order.setOrderType(orderType);
		order.setTimestamps(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.PENDING);
		order.setUser(user);
		return orderRepository.save(order);
	}

	@Override
	public Order getOrderById(Long orderId) throws Exception {
		
		return orderRepository.findById(orderId).orElseThrow(()->new Exception("order not found"));
	}

	@Override
	public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
	
		return orderRepository.getByUserId(userId);
	}

	public OrderItem createOrderItem(Coin coin, double quantity,double buyPrice ,double sellPrice) {
		OrderItem orderItem = new OrderItem();
		orderItem.setBuyPrice(buyPrice);
		orderItem.setCoin(coin);
		orderItem.setQuantity(quantity);
		orderItem.setSellPrice(sellPrice);
		return orderItemRepository.save(orderItem);
	}
	@Transactional
	public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
		if(quantity<=0) {
			throw new Exception("quantity should be >0");
		}
		OrderItem orderItem = createOrderItem(coin,quantity,coin.getCurrentPrice(),0);
		Order order = createOrder(user,orderItem,OrderType.BUY);
		orderItem.setOrder(order);
		walletService.payOrderPayment(order, user);
		order.setOrderStatus(OrderStatus.SUCCESS);
		Order savedOrder = orderRepository.save(order);
		
		
		Asset oldAsset = assetService.findAssetByCoinIdAndUserId(order.getUser().getId(), order.getOrderItem().getCoin().getId());
		if(oldAsset==null) {
			assetService.createAsset(user, coin, quantity);
		}
		else {
			assetService.updateAsset(oldAsset.getId(), quantity);
		}
		return savedOrder;
	}
	
	@Transactional
	public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
		if(quantity<=0) {
			throw new Exception("quantity should be >0");
		}
		
		
		
		Asset assetTosell = assetService.findAssetByCoinIdAndUserId(user.getId(), coin.getId());
		if(assetTosell!=null) {
		double buyPrice = assetTosell.getBuyPrice();
		OrderItem orderItem = createOrderItem(coin,quantity,buyPrice,coin.getCurrentPrice());
		Order order = createOrder(user,orderItem,OrderType.SELL);
		orderItem.setOrder(order);
		
		if(assetTosell.getQuantity()>=quantity) {
			order.setOrderStatus(OrderStatus.SUCCESS);
			Order savedOrder = orderRepository.save(order);
			walletService.payOrderPayment(order, user);
			Asset updatedAsset = assetService.updateAsset(assetTosell.getId(),-quantity);
			if(updatedAsset.getQuantity()*coin.getCurrentPrice()<=1) {
				assetService.deleteAsset(updatedAsset.getId());
			}
			return savedOrder;
		}
		
		throw new Exception("Insufficident quantity to sell");
		}
		throw new Exception("Asset not found");
	
	}
	@Override
	@Transactional
	public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
		if(orderType.equals(OrderType.BUY)) {
			return buyAsset(coin, quantity, user);
		}
		else if(orderType.equals(OrderType.SELL)) {
			return sellAsset(coin, quantity, user);
		}
		throw new Exception("Invalid order type");
		
	}

}
