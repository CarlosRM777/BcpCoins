package com.bcp.coins.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class ExchangeRate {
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
}
