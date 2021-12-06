package com.bcp.coins.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExchangeRateUpdResponse {
	private Long id;
	private String monedaOrigen;
	private String monedaDestino;
	private BigDecimal tipoCambio;
}
