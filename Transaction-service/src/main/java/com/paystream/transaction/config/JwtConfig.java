package com.paystream.transaction.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.Base64;

/**
 * JWT configuration for Transaction Service.
 *
 * Transaction Service validates the JWT independently instead of trusting the
 * API Gateway alone.
 *
 * This creates a second security boundary inside the microservice.
 */
@Configuration
public class JwtConfig {

	@Value("${jwt.secret}")
	private String jwtSecret;

	/**
	 * Creates the symmetric key used to verify HS256 JWT signatures.
	 *
	 * The same secret is shared securely between Auth Service, API Gateway and
	 * protected downstream services.
	 */
	@Bean
	public SecretKey jwtSecretKey() {

		byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);

		return new SecretKeySpec(keyBytes, "HmacSHA256");
	}

	/**
	 * Creates the JWT decoder used by Spring Security.
	 */
	@Bean
	public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {

		return NimbusJwtDecoder.withSecretKey(jwtSecretKey).build();
	}
}