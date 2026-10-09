package com.paystream.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record MerchantProfileRequest(

		@NotBlank(message = "Business name is required") String businessName,

		@NotBlank(message = "Category is required") String category) {
}