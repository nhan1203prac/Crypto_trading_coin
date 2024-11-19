package com.crypto.treading.Modal;

import com.crypto.treading.Domain.VerificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class VerifycationCode {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String otp;
	@OneToOne
	private User user;
	private String email;
	private String mobile;
	private VerificationType verificationType;
}
