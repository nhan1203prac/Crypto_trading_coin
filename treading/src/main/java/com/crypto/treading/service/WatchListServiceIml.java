package com.crypto.treading.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Modal.Coin;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.WatchList;
import com.crypto.treading.repository.WatchListRepository;
@Service
public class WatchListServiceIml implements WatchListService{
	@Autowired
	private WatchListRepository watchListRepository;
	
	@Override
	public WatchList findUserWatchList(Long userId) throws Exception {
		WatchList watchList = watchListRepository.findByUserId(userId);
		if(watchList == null) {
			throw new Exception("watchlist not found");
		}
		return watchList;
	}

	@Override
	public WatchList createWatchList(User user) {
		WatchList watchList = new WatchList();
		watchList.setUser(user);
		return watchListRepository.save(watchList);
	}

	@Override
	public WatchList findById(Long id) throws Exception {
		Optional<WatchList> watchListOptional = watchListRepository.findById(id);
		if(watchListOptional.isEmpty()) {
			throw new Exception("watchList not found");
		}
		return watchListOptional.get();
	}

	@Override
	public Coin addItemToWatchList(Coin coin, User user) throws Exception {
		WatchList watchlist = findUserWatchList(user.getId());
		
		if(watchlist.getCoins().contains(coin)) {
			watchlist.getCoins().remove(coin);
		}
		else {
			watchlist.getCoins().add(coin);
			
		}
		watchListRepository.save(watchlist);
		
		return coin;
	}

}
