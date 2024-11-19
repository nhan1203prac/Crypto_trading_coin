package com.crypto.treading.request;

import com.crypto.treading.Domain.VerificationType;

import lombok.Data;
@Data
public class ForgotPasswordTokenRequest {
	private String sendTo;
	private VerificationType verificationType;
}
