package com.paystream.fraud.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Bean
	public SecretKey jwtSecretKey() {

		byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);

		return new SecretKeySpec(keyBytes, "HmacSHA256");
	}

	@Bean
	public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {

		return NimbusJwtDecoder.withSecretKey(jwtSecretKey).build();
	}
}