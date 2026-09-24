package com.paystream.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRegisterRequest(

        @NotBlank
        String username,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password

) implements RegisterRequest {
}