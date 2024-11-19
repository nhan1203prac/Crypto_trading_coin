package com.crypto.treading.Modal;

import java.time.LocalDate;

import com.crypto.treading.Domain.WalletTransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class WalletTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	private Wallet wallet;
	private WalletTransactionType walletTransactionType;
	private LocalDate datel;
	private Long transferId;
	private String purpose;
	private Long amount;
	
	
	
}
