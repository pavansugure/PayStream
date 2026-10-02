package com.paystream.transaction.client;

import com.paystream.transaction.client.dto.FraudCheckRequest;
import com.paystream.transaction.client.dto.FraudCheckResponse;
import com.paystream.transaction.config.FeignSecurityConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "fraud-service",
        configuration = FeignSecurityConfig.class
)
public interface FraudClient {

    @PostMapping("/api/fraud/check")
    FraudCheckResponse checkTransaction(
            @RequestBody FraudCheckRequest request
    );
}