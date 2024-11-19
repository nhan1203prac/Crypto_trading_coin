package com.crypto.treading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.treading.Modal.Coin;
import com.crypto.treading.service.CoinService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/coins")
public class CoinController {
	@Autowired
	private CoinService coinService;
	
	private ObjectMapper objectMapper;
	
	@GetMapping
	ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception{
		List<Coin> coins = coinService.getCoinList(page);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(coins);
	}
	
	@GetMapping("/{coinId}/chart")
	ResponseEntity<JsonNode> getMarketChart(@RequestParam("days") int days, @PathVariable("coinId") String coinId) throws Exception{
		String res = coinService.getMarketChar(coinId, days);
//		System.out.println("-------marketChart" + res);
		objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(res);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(jsonNode);
	}
	
	@GetMapping("/details/{coinId}")
	ResponseEntity<JsonNode>getCoinDetails(@PathVariable("coinId") String coinId) throws Exception{
		String res = coinService.getCoinDetail(coinId);
		objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(res);
		return ResponseEntity.status(HttpStatus.OK).body(node);
	}
	@GetMapping("/search")
	ResponseEntity<JsonNode>searchCoin(@RequestParam("q") String keyword) throws Exception {
		String res = coinService.searchCoin(keyword);
		JsonNode node = objectMapper.readTree(res);
		return ResponseEntity.status(HttpStatus.OK).body(node);
	}
	
	@GetMapping("/top50")
	ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception{
		String res = coinService.getTop50CoinsByMarketCapRank();
		JsonNode node = objectMapper.readTree(res);
		return ResponseEntity.status(HttpStatus.OK).body(node);
	}
	@GetMapping("/{coinId}")
	ResponseEntity<Coin> getCoinById(@PathVariable("coinId")String coinId ) throws Exception{
		Coin res = coinService.findById(coinId);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	@GetMapping("/treading")
	ResponseEntity<JsonNode> getTreadingCoin() throws Exception{
		String res = coinService.getTreadingCoin();
		objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(res);
		return ResponseEntity.status(HttpStatus.OK).body(node);
	}
}
