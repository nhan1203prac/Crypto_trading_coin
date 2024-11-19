package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
