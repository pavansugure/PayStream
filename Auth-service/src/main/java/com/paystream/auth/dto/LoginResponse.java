package com.paystream.auth.dto;

/**
 * Java 21 record used as the response DTO for successful login.
 *
 * The JWT is returned to the client, which will later send it in the
 * Authorization header for protected requests.
 */
public record LoginResponse(String accessToken, String tokenType) {
}