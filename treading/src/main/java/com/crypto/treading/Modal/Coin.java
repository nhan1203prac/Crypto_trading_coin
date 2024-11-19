package com.crypto.treading.Modal;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Coin {
		@Id
		@JsonProperty("id")
		private String id;
		
		@JsonProperty("symbol")
	    private String symbol;
		
		@JsonProperty("name")
	    private String name;
		
		@JsonProperty("image")
	    private String image;

	    @JsonProperty("current_price")
	    private Double currentPrice;

	    @JsonProperty("market_cap")
	    private long marketCap;

	    @JsonProperty("market_cap_rank")
	    private Integer marketCapRank;

	    @JsonProperty("fully_diluted_valuation")
	    private Double fullyDilutedValuation;

	    @JsonProperty("total_volume")
	    private long totalVolume;

	    @JsonProperty("high_24h")
	    private Double high24h;

	    @JsonProperty("low_24h")
	    private Double low24h;

	    @JsonProperty("price_change_24h")
	    private Double priceChange24h;

	    @JsonProperty("price_change_percentage_24h")
	    private Double priceChangePercentage24h;

	    @JsonProperty("market_cap_change_24h")
	    private long marketCapChange24h;

	    @JsonProperty("market_cap_change_percentage_24h")
	    private Double marketCapChangePercentage24h;

	    @JsonProperty("circulating_supply")
	    private long circulatingSupply;

	    @JsonProperty("total_supply")
	    private long totalSupply;

	    @JsonProperty("max_supply")
	    private long maxSupply; // có thể là null

	    @JsonProperty("ath")
	    private Double ath;

	    @JsonProperty("ath_change_percentage")
	    private Double athChangePercentage;

	    @JsonProperty("ath_date")
	    private Date athDate;

	    @JsonProperty("atl")
	    private Double atl;

	    @JsonProperty("atl_change_percentage")
	    private Double atlChangePercentage;

	    @JsonProperty("atl_date")
	    private Date atlDate;

	    @JsonProperty("roi")
	    @JsonIgnore
	    private String roi; // có thể là null

	    @JsonProperty("last_updated")
	    private Date lastUpdated;
}
