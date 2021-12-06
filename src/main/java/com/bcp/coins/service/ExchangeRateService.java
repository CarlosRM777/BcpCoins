package com.bcp.coins.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcp.coins.dto.ExchangeRateUpdRequest;
import com.bcp.coins.dto.ExchangeRateUpdResponse;
import com.bcp.coins.model.Coin;
import com.bcp.coins.model.ExchangeRate;
import com.bcp.coins.repository.ExchangeRateRepository;

import rx.Single;


@Service
@Transactional
public class ExchangeRateService {
	@Autowired
	private ExchangeRateRepository exchangeRateRepository;
	@Autowired
	private CoinService coinService;
	private BigDecimal exchangeRateChanged;
	
	public Single<ExchangeRate> getExchangeRateByShortNames(String originCoin, String destinyCoin) {
		return Single.create(singleSubscriber -> {
			Optional<ExchangeRate> optExRate = exchangeRateRepository.findByOriginCoinShortNameAndDestinationCoinShortName(originCoin, destinyCoin);
			if (optExRate.isPresent()) 
				singleSubscriber.onSuccess(optExRate.get());
			else
				singleSubscriber.onError(new EntityNotFoundException("Tipo de Cambio " + originCoin + " a "+ destinyCoin +" No existe"));
		}); 
	}
	
	public Single<ExchangeRateUpdResponse> updExchangeRate(ExchangeRateUpdRequest exchangeRateUpdResquest) {
		return Single.create(singleSubscriber -> {
			exchangeRateChanged = exchangeRateUpdResquest.getTipoCambio();
			Single<Coin> sOriCoin = coinService.getCoinByShortName(exchangeRateUpdResquest.getMonedaOrigen());
			Single<Coin> sDesCoin = coinService.getCoinByShortName(exchangeRateUpdResquest.getMonedaDestino());
			ExchangeRateUpdResponse exchangeUpdRes = Single.zip(sOriCoin, sDesCoin, this::updExchangeRate).toBlocking().value();
			singleSubscriber.onSuccess(exchangeUpdRes);
			singleSubscriber.onError(new Exception("Error General en el Sistema"));
		});
	}
	
	public Single<List<ExchangeRateUpdResponse>> findAllExchangeRates() {
		return Single.create(singleSubscriber -> {
			List<ExchangeRateUpdResponse> listExchangeRate = exchangeRateRepository.findAll().
					parallelStream().map(exchangeRate -> {
								return ExchangeRateUpdResponse.builder().id(exchangeRate.getId())
										.monedaOrigen(exchangeRate.getOriginCoin().getShortName())
										.monedaDestino(exchangeRate.getDestinationCoin().getShortName())
										.tipoCambio(exchangeRate.getExchangeRate()).build();
					}).collect(Collectors.toList());
			singleSubscriber.onSuccess(listExchangeRate);
			singleSubscriber.onError(new Exception("Error General en el Sistema"));
		});
	}
	
	private ExchangeRateUpdResponse updExchangeRate(Coin pOrigin, Coin pDestination) {
		try {
			ExchangeRate exchangeRate = this.getExchangeRateByShortNames(pOrigin.getShortName(), pDestination.getShortName())
					.toBlocking().value();
			exchangeRate.setExchangeRate(exchangeRateChanged);
			exchangeRateRepository.save(exchangeRate);
			return ExchangeRateUpdResponse.builder()
					.id(exchangeRate.getId())
					.monedaOrigen(pOrigin.getShortName())
					.monedaDestino(pDestination.getShortName())
					.tipoCambio(exchangeRate.getExchangeRate()).build();
		}
		catch(Exception ex) {
			throw ex;
		}
			
	}
}
