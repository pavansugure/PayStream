package com.paystream.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Java 21 record used as an immutable DTO for login requests.
 *
 * Records are appropriate here because this object only carries request data
 * and does not need mutable state.
 *
 * Java automatically provides the accessor methods: username() password()
 */
public record LoginRequest(

		@NotBlank String username,

		@NotBlank String password

) {
}