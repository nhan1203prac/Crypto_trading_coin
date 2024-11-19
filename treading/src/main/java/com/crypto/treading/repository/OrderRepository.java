package com.crypto.treading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> getByUserId(Long userId);
}
