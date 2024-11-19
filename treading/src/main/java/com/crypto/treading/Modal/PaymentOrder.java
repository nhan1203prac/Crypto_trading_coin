package com.crypto.treading.Modal;

import com.crypto.treading.Domain.PaymentMethod;
import com.crypto.treading.Domain.PaymentOrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data

public class PaymentOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long amount;
	private PaymentOrderStatus status;
	
	private PaymentMethod PaymentMethod;
	
	@ManyToOne
	private User user;
}
