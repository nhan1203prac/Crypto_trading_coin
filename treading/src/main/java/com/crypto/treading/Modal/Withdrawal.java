package com.crypto.treading.Modal;

import java.time.LocalDateTime;

import com.crypto.treading.Domain.WithdrawalStatus;
import com.crypto.treading.Modal.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Withdrawal {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private WithdrawalStatus status;
	
	@ManyToOne
	private User user;
	
	private Long amount;
	
	private LocalDateTime date = LocalDateTime.now();
	
	
}
