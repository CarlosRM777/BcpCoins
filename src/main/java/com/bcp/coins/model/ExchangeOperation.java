package com.bcp.coins.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class ExchangeOperation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oric_fk", referencedColumnName = "id")
	private Coin originCoin;
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desc_fk", referencedColumnName = "id")
	private Coin destinationCoin;
	private BigDecimal exchangeRate;
	private BigDecimal inicialAmount;
	private BigDecimal exchangedAmount;
	private LocalDateTime operationDate;
	//idcliente
	//cuentaorigen
	//cuentadestino
}
