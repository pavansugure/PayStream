package com.paystream.transaction.client;

import com.paystream.transaction.client.dto.MerchantStatusResponse;
import com.paystream.transaction.config.FeignSecurityConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", configuration = FeignSecurityConfig.class)
public interface MerchantValidationClient {

	@GetMapping("/api/auth/merchants/{merchantId}")
	MerchantStatusResponse getMerchantStatus(@PathVariable Long merchantId);
}