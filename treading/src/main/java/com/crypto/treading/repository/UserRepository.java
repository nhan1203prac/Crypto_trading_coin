package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);

}
