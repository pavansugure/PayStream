package com.paystream.gateway.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;

import java.util.Base64;

/**
 * JWT configuration for the API Gateway.
 *
 * The Gateway does not issue JWTs. It validates JWTs issued by the Auth
 * Service.
 *
 * Because Spring Cloud Gateway is reactive/WebFlux based, the Gateway requires
 * a ReactiveJwtDecoder.
 */
@Configuration
public class JwtConfig {

	/*
	 * The actual secret is supplied through the environment variable
	 * PAYSTREAM_JWT_SECRET.
	 *
	 * We deliberately do not hardcode the secret in Java or application.properties.
	 */
	@Value("${jwt.secret}")
	private String jwtSecret;

	/**
	 * Creates the cryptographic key used to verify JWT signatures.
	 *
	 * The Auth Service signs JWTs using the same secret. Since HS256 is a symmetric
	 * algorithm, the Gateway uses the same secret to verify the JWT signature.
	 */
	@Bean
	public SecretKey jwtSecretKey() {

		byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);

		return new SecretKeySpec(keyBytes, "HmacSHA256");
	}

	/**
	 * Creates the reactive JWT decoder required by Spring Security WebFlux.
	 *
	 * NimbusReactiveJwtDecoder performs the cryptographic verification of incoming
	 * JWT signatures.
	 */
	@Bean
	public ReactiveJwtDecoder jwtDecoder(SecretKey jwtSecretKey) {

		return NimbusReactiveJwtDecoder.withSecretKey(jwtSecretKey).build();
	}
}