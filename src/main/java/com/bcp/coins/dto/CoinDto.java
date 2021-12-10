package com.bcp.coins.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CoinDto {
	private String shortName;
	private String largeName;
}
