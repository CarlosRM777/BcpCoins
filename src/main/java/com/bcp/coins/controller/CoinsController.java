package com.bcp.coins.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcp.coins.dto.ExchangeOperationRequest;
import com.bcp.coins.dto.ExchangeRateUpdRequest;
import com.bcp.coins.dto.ExchangeRateUpdResponse;
import com.bcp.coins.model.ErrorMessage;
import com.bcp.coins.security.JWTServiceAuth0;
import com.bcp.coins.service.ExchangeOperationService;
import com.bcp.coins.service.ExchangeRateService;
import com.fasterxml.jackson.core.JsonFactory;

import rx.schedulers.Schedulers;

@PreAuthorize("authenticated")
@RestController
@RequestMapping("v1/exchange")
public class CoinsController {
	@Autowired
	private ExchangeOperationService exchangeOperationService;
	@Autowired
	private ExchangeRateService exchangeRateService;
	
	@Autowired
	private JWTServiceAuth0 lJwtService;
	
	private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CoinsController.class);
	
	@PostMapping("/")
	public ResponseEntity<?> createMoneyExchange(final @RequestBody ExchangeOperationRequest exchangeOperationRequest) {
		try {
			logger.info("Post Request received for v1/exchange/ with Request Body: {}", exchangeOperationRequest);
			return exchangeOperationService.createExchangeOperation(exchangeOperationRequest).subscribeOn(Schedulers.io()).map(
	            s -> ResponseEntity.created(URI.create("/v1/exchange/1")).body(s)).toBlocking().value();
		}
		catch(Exception ex) {
			logger.error("Error on request received for v1/exchange/ with Request Body: {} with error: {}", exchangeOperationRequest, ex.getMessage());
			return ResponseEntity.badRequest().body(new ErrorMessage(ex, "v1/exchange"));
		}
	}
	
	@PutMapping("/coin")
	public ResponseEntity<?> updCoinValue(final @RequestBody ExchangeRateUpdRequest tipoCambioUpdRequest) {
		try {
			logger.info("Put Request received for v1/exchange/coin with Request Body: {}", tipoCambioUpdRequest);
			return exchangeRateService.updExchangeRate(tipoCambioUpdRequest).subscribeOn(Schedulers.io()).map(
	            s -> ResponseEntity.ok().body(s)).toBlocking().value();
		}
		catch(Exception ex) {
			logger.error("Error on request received for v1/exchange/ with Request Body: {} with error: {}", tipoCambioUpdRequest, ex.getMessage());
			return ResponseEntity.badRequest().body(new ErrorMessage(ex, "v1/exchange/coin"));
		}
	}
	
	@GetMapping("/coin")
	public ResponseEntity<?> getAllValueExchanges() {
		try {
			logger.info("Get Request received for v1/exchange/coin");
			return exchangeRateService.findAllExchangeRates().subscribeOn(Schedulers.io()).map(
	            s -> ResponseEntity.ok().body(s)).toBlocking().value();
		}
		catch(Exception ex) {
			logger.error("Error on request received for v1/exchange/coin with error: {}", ex.getMessage());
			return ResponseEntity.badRequest().body(new ErrorMessage(ex, "v1/exchange/coin"));
		}
	}
	
	@GetMapping("/coin/{ori}/{des}")
	public ResponseEntity<ExchangeRateUpdResponse> getValueExchange(final @PathVariable Long ori, final @PathVariable Long des) {
		return null;
	}
	
	@PostMapping("/token")
	public ResponseEntity<?> getToken(final @AuthenticationPrincipal User activeUser) {
		logger.info("Get Request received for v1/exchange/token");
		try {
			List<String> authorities = new ArrayList<>();
			for (GrantedAuthority role : activeUser.getAuthorities()) 
				authorities.add(role.getAuthority());	
			HashMap<String, String> hashMap = new HashMap();
			hashMap.put("token", lJwtService.createToken(activeUser.getUsername(), authorities));
			return ResponseEntity.ok(hashMap);
		} catch (Exception ex) {
			logger.error("Error on request received for v1/exchange/token with error: {}", ex.getMessage());
			return ResponseEntity.badRequest().body(new ErrorMessage(ex, "v1/exchange/coin"));
		}
	}
}
