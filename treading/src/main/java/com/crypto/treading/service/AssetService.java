package com.crypto.treading.service;

import java.util.List;

import com.crypto.treading.Modal.Asset;
import com.crypto.treading.Modal.Coin;
import com.crypto.treading.Modal.User;

public interface AssetService {
	Asset createAsset(User user,Coin coin,double quantity );
	Asset getAssetById(Long assetId) throws Exception;
	Asset getAssetByUserIdAndId(Long userId, Long assetId);
	List<Asset> getAllAssetOfUser(Long userId);
	Asset updateAsset(Long assetId, double quantity) throws Exception;
	Asset findAssetByCoinIdAndUserId(Long userId, String coinId);
	void deleteAsset(Long assetId);
}
