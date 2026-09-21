package com.paystream.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Java 21 record:
 * Records are ideal for DTOs because this object is only used
 * to carry immutable request data between the controller and service layer.
 *
 * Java automatically provides:
 * - private final fields
 * - constructor
 * - accessor methods: username(), email(), password()
 * - equals()
 * - hashCode()
 * - toString()
 */
public record RegisterRequest(

        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password

) {
}