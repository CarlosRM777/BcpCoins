package com.bcp.coins.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExchangeOperationResponse {
	private BigDecimal montoInicial;
	private BigDecimal montoCambiado;
	private String monedaOrigen;
	private String monedaDestino;
	private BigDecimal tipoCambio;
}
