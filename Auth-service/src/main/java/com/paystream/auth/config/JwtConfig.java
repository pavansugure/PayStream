package com.paystream.auth.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.util.Base64;

/**
 * JWT configuration for the Auth Service.
 *
 * This class is responsible for creating and exposing the JwtEncoder as a
 * Spring Bean.
 *
 * The AuthServiceImpl will use this encoder when it needs to generate a signed
 * JWT after successful login.
 */
@Configuration
public class JwtConfig {

	/*
	 * The JWT secret is supplied from an environment variable through
	 * application.properties. We do NOT hardcode the secret in Java because secrets
	 * should never be committed to GitHub.
	 */
	@Value("${jwt.secret}")
	private String jwtSecret;

	/**
	 * Creates the cryptographic key used to sign JWTs. Base64 decoding converts the
	 * external secret representation into the raw bytes required by the HMAC
	 * algorithm.
	 */
	@Bean
	public SecretKey jwtSecretKey() {

		byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);

		return new SecretKeySpec(keyBytes, "HmacSHA256");
	}

	/**
	 * Creates the Spring Security JwtEncoder. NimbusJwtEncoder performs the actual
	 * cryptographic signing of the JWT.
	 */
	@Bean
	public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {

		return NimbusJwtEncoder.withSecretKey(jwtSecretKey).algorithm(MacAlgorithm.HS256).build();
	}

	@Bean
	public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
		return NimbusJwtDecoder.withSecretKey(jwtSecretKey).build();
	}
}