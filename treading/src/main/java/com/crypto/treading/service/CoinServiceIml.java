package com.crypto.treading.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.crypto.treading.Modal.Coin;
import com.crypto.treading.repository.CoinRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import aj.org.objectweb.asm.TypeReference;
@Service
public class CoinServiceIml implements CoinService{
	@Autowired
	private CoinRepository coinRepository;
	
	@Override
	public List<Coin> getCoinList(int pae) throws Exception {
		// TODO Auto-generated method stub
		String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page="+pae;
		List<Coin> coinList = new ArrayList<>();
		try {
			HttpHeaders header = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters",header);
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> response = template.getForEntity(url,String.class,entity);
			
			ObjectMapper ob = new ObjectMapper();
			coinList = ob.readValue(response.getBody(),new com.fasterxml.jackson.core.type.TypeReference<List<Coin>>() {
			});
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
		return coinList;
	}

	@Override
	public String getMarketChar(String coinId, int days) throws Exception {
	    String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
	    int maxRetries = 3;
	    int retryCount = 0;
	    int waitTime = 10000;

	    while (retryCount < maxRetries) {
	        try {
	            HttpHeaders headers = new HttpHeaders();
	            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
	            RestTemplate template = new RestTemplate();
	            ResponseEntity<String> response = template.getForEntity(url, String.class, entity);

	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode root = mapper.readTree(response.getBody());
	            JsonNode prices = root.path("prices");

	            // Filter the prices to include every 10th data point
	            ArrayNode filteredPrices = mapper.createArrayNode();
	            for (int i = 0; i < prices.size(); i += 5) { // Take every 10th element
	                filteredPrices.add(prices.get(i));
	            }

	            // Replace original prices with filtered data
	            

	            return mapper.writeValueAsString(filteredPrices);

	        } catch (HttpClientErrorException e) {
	            if (e.getStatusCode().value() == 429) {
	                System.out.println("Rate limit exceeded. Waiting before retrying...");
	                Thread.sleep(waitTime);
	                retryCount++;
	            } else {
	                throw new Exception(e.getMessage());
	            }
	        }
	    }
	    throw new Exception("Exceeded maximum retries for rate limit");
	}


	@Override
	public String getCoinDetail(String coinId) throws Exception {
		String url = "https://api.coingecko.com/api/v3/coins/"+coinId;
		List<Coin> coinList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();	
		try {
			HttpHeaders header = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters",header);
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> response = template.getForEntity(url,String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			Coin coin = new Coin();
			coin.setId(jsonNode.get("id").asText());
			coin.setSymbol(jsonNode.get("symbol").asText());
			coin.setName(jsonNode.get("name").asText());
			coin.setImage(jsonNode.get("image").get("large").asText());
			JsonNode marketData = jsonNode.get("market_data");
			
			coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
			coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
			coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
			coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
			coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
			coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
			coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
			coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
			coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
			coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());
			coin.setTotalSupply(marketData.get("total_supply").asLong());
			
			coinRepository.save(coin);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public Coin findById(String coinId) throws Exception {
		Optional<Coin> coin = coinRepository.findById(coinId);
		if(coin.isEmpty()) throw new Exception("Coin not found");
		return coin.get();
	}

	@Override
	public String searchCoin(String keyword) throws Exception {
		String url = "https://api.coingecko.com/api/v3/search?query="+keyword;
//		List<Coin> coinList = new ArrayList<>();
		try {
			HttpHeaders header = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters",header);
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> response = template.getForEntity(url,String.class,entity);
			
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public String getTop50CoinsByMarketCapRank() throws Exception {
		String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50";
//		List<Coin> coinList = new ArrayList<>();
		try {
			HttpHeaders header = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters",header);
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> response = template.getForEntity(url,String.class,entity);
			
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public String getTreadingCoin() throws Exception {
		String url = "https://api.coingecko.com/api/v3/search/trending";
//		List<Coin> coinList = new ArrayList<>();
		try {
			HttpHeaders header = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters",header);
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> response = template.getForEntity(url,String.class,entity);
			
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}

}
