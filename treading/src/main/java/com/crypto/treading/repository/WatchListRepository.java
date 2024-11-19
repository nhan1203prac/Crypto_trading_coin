package com.crypto.treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crypto.treading.Modal.WatchList;

public interface WatchListRepository extends JpaRepository<WatchList, Long>{
	WatchList findByUserId(Long userId);
}
