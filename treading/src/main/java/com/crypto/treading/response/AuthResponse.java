package com.crypto.treading.response;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class AuthResponse {
	private String jwt;
	private String message;
	private boolean status;
	private boolean isTwoFatorAuthEnabled;
	private String session;
}
