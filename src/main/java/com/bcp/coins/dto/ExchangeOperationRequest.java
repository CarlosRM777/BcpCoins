package com.bcp.coins.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class ExchangeOperationRequest {
	private BigDecimal monto;
	private String monedaOrigen;
	private String monedaDestino;
}
