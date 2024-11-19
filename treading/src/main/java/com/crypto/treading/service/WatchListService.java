package com.crypto.treading.service;

import com.crypto.treading.Modal.Coin;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.WatchList;

public interface WatchListService {
	WatchList findUserWatchList(Long userId) throws Exception;
	WatchList createWatchList(User user);
	WatchList findById(Long id) throws Exception;
	Coin addItemToWatchList(Coin coin, User user) throws Exception;
}
