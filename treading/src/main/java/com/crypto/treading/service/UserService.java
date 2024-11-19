package com.crypto.treading.service;

import com.crypto.treading.Domain.VerificationType;
import com.crypto.treading.Modal.User;

public interface UserService {
	public User findUserProfileByJwt(String jwt) throws Exception;
	public User findUserByEmail(String email) throws Exception;
	public User findUserById(Long userId) throws Exception;
	public User enableTwoFactorAuthentication(VerificationType verifycationType,String sendTo, User user);
	User updatePassword(User user,String newPassword);
}
