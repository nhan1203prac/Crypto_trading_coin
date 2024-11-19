package com.crypto.treading.request;

import com.crypto.treading.Domain.OrderType;

import lombok.Data;

@Data
public class createOrderRequest {
	private String coinId;
	private double quantity;
	private OrderType orderType;
}
