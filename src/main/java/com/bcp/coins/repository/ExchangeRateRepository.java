package com.bcp.coins.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bcp.coins.model.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
	public Optional<ExchangeRate> findByOriginCoinShortNameAndDestinationCoinShortName(String origin, String destination);
}
