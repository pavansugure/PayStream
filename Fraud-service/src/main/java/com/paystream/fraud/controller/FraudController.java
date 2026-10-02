package com.paystream.fraud.controller;

import com.paystream.fraud.dto.FraudCheckRequest;
import com.paystream.fraud.dto.FraudCheckResponse;
import com.paystream.fraud.service.FraudService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

	private final FraudService fraudService;

	public FraudController(FraudService fraudService) {
		this.fraudService = fraudService;
	}

	@PostMapping("/check")
	public FraudCheckResponse checkTransaction(@Valid @RequestBody FraudCheckRequest request) {

		return fraudService.checkTransaction(request);
	}
}