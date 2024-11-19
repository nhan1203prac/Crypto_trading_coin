package com.crypto.treading.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Modal.Asset;
import com.crypto.treading.Modal.User;
import com.crypto.treading.service.AssetServiceIml;
import com.crypto.treading.service.UserServiceIml;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
	@Autowired
	private AssetServiceIml assetService;
	
	@Autowired
	private UserServiceIml userService;
	
	@GetMapping("/{assetId}")
	public ResponseEntity<Asset> getAssetById(@PathVariable("assetId") Long assetId) throws Exception{
		Asset asset = assetService.getAssetById(assetId);
		return ResponseEntity.status(HttpStatus.OK).body(asset);
	}
	@GetMapping("/coin/{coinId}/user")
	public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@PathVariable("coinId") String coinId, @RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserProfileByJwt(jwt);
		Asset asset = assetService.findAssetByCoinIdAndUserId(user.getId(), coinId);
		return ResponseEntity.ok().body(asset);
	}
	@GetMapping()
	public ResponseEntity<List<Asset>> getAllAssetOfUser(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserProfileByJwt(jwt);
		List<Asset> asset = assetService.getAllAssetOfUser(user.getId());
		return ResponseEntity.ok().body(asset);
	}
}
