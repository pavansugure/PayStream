package com.paystream.auth.dto;

public record MerchantProfileResponse(

        Long merchantId,

        Long userId,

        String username,

        String email,

        String businessName,

        String category,

        boolean verified
) {
}