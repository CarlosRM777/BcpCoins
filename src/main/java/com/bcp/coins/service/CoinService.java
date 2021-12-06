package com.bcp.coins.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bcp.coins.repository.CoinRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import rx.Single;

import com.bcp.coins.model.Coin;

@Service
@Transactional
public class CoinService {
	@Autowired
	private CoinRepository coinRepository;

	public Single<Coin> getCoinByShortName(String originCoin) {
		return Single.create(singleSubscriber -> {
			Optional<Coin> optCoin = coinRepository.findByShortName(originCoin);
			if (optCoin.isPresent()) 
				singleSubscriber.onSuccess(optCoin.get());
			else
				singleSubscriber.onError(new EntityNotFoundException("Moneda "+ originCoin+" No Existe"));
		}); 
	}
	
}
