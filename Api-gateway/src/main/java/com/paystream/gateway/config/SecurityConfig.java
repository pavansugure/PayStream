package com.paystream.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for the API Gateway.
 *
 * The Gateway is responsible for validating JWT access tokens before requests
 * are forwarded to downstream microservices.
 *
 * Because Spring Cloud Gateway is reactive/WebFlux based, we use
 * SecurityWebFilterChain instead of the servlet-based SecurityFilterChain.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	/**
	 * Defines the reactive Spring Security filter chain.
	 *
	 * Authentication endpoints are public because a user does not have a JWT before
	 * registration or login.
	 *
	 * All other Gateway requests require a valid JWT.
	 */
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

		return http

				/*
				 * Our PayStream APIs are stateless and use JWTs rather than browser sessions.
				 *
				 * Therefore, CSRF protection is disabled.
				 */
				.csrf(ServerHttpSecurity.CsrfSpec::disable)

				/*
				 * Define which Gateway routes are publicly accessible.
				 */
				.authorizeExchange(
						exchanges -> exchanges.pathMatchers("/api/auth/register", "/api/auth/login").permitAll()

								/*
								 * Every other request must have a valid JWT.
								 */
								.anyExchange().authenticated())

				/*
				 * Enable OAuth2 Resource Server JWT authentication.
				 *
				 * Spring Security will extract:
				 *
				 * Authorization: Bearer <JWT>
				 *
				 * and use our JwtDecoder to validate it.
				 */
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
				}))

				.build();
	}
}