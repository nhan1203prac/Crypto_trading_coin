package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.Coin;

public interface CoinRepository extends JpaRepository<Coin, String>{
	
}
