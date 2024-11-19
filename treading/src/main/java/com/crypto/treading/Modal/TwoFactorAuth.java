package com.crypto.treading.Modal;

import com.crypto.treading.Domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {
	private boolean isEnabled = false;
	private VerificationType sendTo;
}
