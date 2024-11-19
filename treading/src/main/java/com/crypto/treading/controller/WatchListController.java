package com.crypto.treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Modal.Coin;
import com.crypto.treading.Modal.User;
import com.crypto.treading.Modal.WatchList;
import com.crypto.treading.service.CoinServiceIml;
import com.crypto.treading.service.UserServiceIml;
import com.crypto.treading.service.WatchListService;
import com.crypto.treading.service.WatchListServiceIml;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {
	@Autowired
	private WatchListServiceIml watchListServiceIml;
	@Autowired
	private UserServiceIml userServiceIml;
	@Autowired
	private CoinServiceIml coinServiceIml;
	
	@GetMapping("/user")
	public ResponseEntity<WatchList> getUserWatchlist(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		WatchList watchlist = watchListServiceIml.findUserWatchList(user.getId());
		return ResponseEntity.status(HttpStatus.OK).body(watchlist);
		
		
	}
	
	@PostMapping("/create")
	public ResponseEntity<WatchList> createWatchList(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(watchListServiceIml.createWatchList(user));
	}
	
	@GetMapping("/{watchlistId}")
	public ResponseEntity<WatchList> getWatchListById(@PathVariable("watchlistId") Long watchlistId) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(watchListServiceIml.findById(watchlistId));
		
	}
	
	@PatchMapping("/add/coin/{coinId}")
	public ResponseEntity<Coin> addItemToWatchList(@RequestHeader("Authorization") String jwt, @PathVariable("coinId") String coinId) throws Exception{
		User user = userServiceIml.findUserProfileByJwt(jwt);
		Coin coin = coinServiceIml.findById(coinId);
		Coin addItem = watchListServiceIml.addItemToWatchList(coin, user);
		return ResponseEntity.status(HttpStatus.OK).body(addItem);
	}
	
	
	
}
