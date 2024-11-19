package com.crypto.treading.Modal;

import com.crypto.treading.Domain.VerificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class ForgotPasswordToken {
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@OneToOne 
	private User user;
	private String otp;
	private VerificationType verificationType;
	private String sendTo;
}
