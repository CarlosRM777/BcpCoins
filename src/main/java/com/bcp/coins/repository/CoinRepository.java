package com.bcp.coins.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bcp.coins.model.Coin;

public interface CoinRepository extends JpaRepository<Coin, Long> {
	Optional<Coin> findByShortName(String shortName);
}
