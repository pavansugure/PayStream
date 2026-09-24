package com.paystream.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MerchantRegisterRequest(

        @NotBlank
        String username,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotBlank
        String businessName,

        @NotBlank
        String category

) implements RegisterRequest {
}