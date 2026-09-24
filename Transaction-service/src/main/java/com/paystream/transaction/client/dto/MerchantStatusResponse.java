package com.paystream.transaction.client.dto;

public record MerchantStatusResponse(
        Long merchantId,
        Long userId,
        String businessName,
        String category,
        boolean verified
) {
}