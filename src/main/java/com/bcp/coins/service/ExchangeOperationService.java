package com.bcp.coins.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcp.coins.dto.ExchangeOperationRequest;
import com.bcp.coins.dto.ExchangeOperationResponse;
import com.bcp.coins.model.Coin;
import com.bcp.coins.model.ExchangeOperation;
import com.bcp.coins.model.ExchangeRate;
import com.bcp.coins.repository.ExchangeOperationRepository;

import rx.Single;

@Service
@Transactional
public class ExchangeOperationService {
	@Autowired
	private ExchangeOperationRepository exchangeOperationRepository;
	@Autowired
	private CoinService coinService;
	@Autowired
	private ExchangeRateService exchangeRateService;
	
	private BigDecimal initialAmount;
	
	public Single<ExchangeOperationResponse> createExchangeOperation(final ExchangeOperationRequest exchangeRequest) {
		return Single.create(singleSubscriber -> {
			Single<Coin> sOriCoin = coinService.getCoinByShortName(exchangeRequest.getMonedaOrigen());
			Single<Coin> sDesCoin = coinService.getCoinByShortName(exchangeRequest.getMonedaDestino());
			initialAmount = exchangeRequest.getMonto();
			
			ExchangeOperationResponse exchangeOpeRes = Single.zip(sOriCoin, sDesCoin, this::getExchangeRate).toBlocking().value();
			singleSubscriber.onSuccess(exchangeOpeRes);
			singleSubscriber.onError(new Exception("ERROR"));
		});
	}

	private ExchangeOperationResponse getExchangeRate(Coin pOrigin, Coin pDestination) {
		try {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByShortNames(pOrigin.getShortName(), pDestination.getShortName())
					.toBlocking().value();
			BigDecimal exchangeRateValue = exchangeRate.getExchangeRate();
			exchangeOperationRepository.save(
					ExchangeOperation.builder().originCoin(pOrigin)
					.destinationCoin(pDestination).exchangeRate(exchangeRateValue)
					.inicialAmount(initialAmount).exchangedAmount(initialAmount.multiply(exchangeRateValue))
					.operationDate(LocalDateTime.now()).build());
			return ExchangeOperationResponse.builder()
					.monedaOrigen(pOrigin.getShortName())
					.monedaDestino(pDestination.getShortName())
					.tipoCambio(exchangeRateValue).montoInicial(initialAmount).montoCambiado(initialAmount.multiply(exchangeRateValue))
					.build();
		}
		catch(Exception ex) {
			throw ex;
		}
			
	}

}
