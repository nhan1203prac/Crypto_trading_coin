package com.crypto.treading.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.treading.Modal.Asset;
import com.crypto.treading.Modal.Coin;
import com.crypto.treading.Modal.User;
import com.crypto.treading.repository.AssetRepository;

@Service
public class AssetServiceIml implements AssetService {

	@Autowired
	private AssetRepository assetRepository;
	@Override
	public Asset createAsset(User user, Coin coin, double quantity) {
		Asset asset = new Asset();
		asset.setBuyPrice(coin.getCurrentPrice());
		asset.setCoin(coin);
		asset.setQuantity(quantity);
		asset.setUser(user);
		return assetRepository.save(asset);
	}

	@Override
	public Asset getAssetById(Long assetId) throws Exception {
		// TODO Auto-generated method stub
		return assetRepository.findById(assetId).orElseThrow(()->new Exception("asset not found"));
	}

	@Override
	public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asset> getAllAssetOfUser(Long userId) {
		// TODO Auto-generated method stub
		return assetRepository.findByUserId(userId);
	}

	@Override
	public Asset updateAsset(Long assetId, double quantity) throws Exception {
		Asset oldAsset = getAssetById(assetId);
		oldAsset.setQuantity(quantity+oldAsset.getQuantity());
		return assetRepository.save(oldAsset);
	}

	@Override
	public Asset findAssetByCoinIdAndUserId(Long userId, String coinId) {
		// TODO Auto-generated method stub
		return assetRepository.findByUserIdAndCoinId(userId, coinId);
	}

	@Override
	public void deleteAsset(Long assetId) {
		// TODO Auto-generated method stub
		assetRepository.deleteById(assetId);
	}

}
