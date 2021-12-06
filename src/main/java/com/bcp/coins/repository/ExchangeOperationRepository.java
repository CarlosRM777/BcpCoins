package com.bcp.coins.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bcp.coins.model.ExchangeOperation;

public interface ExchangeOperationRepository extends JpaRepository<ExchangeOperation, Long> {
}