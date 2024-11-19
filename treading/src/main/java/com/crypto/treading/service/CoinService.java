package com.crypto.treading.service;

import java.util.List;

import com.crypto.treading.Modal.Coin;

public interface CoinService {
	List<Coin>getCoinList(int pae) throws Exception;
	String getMarketChar(String coinId, int days) throws Exception;
	String getCoinDetail(String coinId) throws Exception;
	Coin findById(String coinId) throws Exception;
	String searchCoin(String keyword) throws Exception;
	String getTop50CoinsByMarketCapRank() throws Exception;
	String getTreadingCoin() throws Exception;
}
