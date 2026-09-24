package com.paystream.transaction.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

@Configuration
public class FeignSecurityConfig {

	/*
	 * Feign creates a new HTTP request when calling another service.
	 *
	 * The incoming Authorization header is therefore not automatically forwarded to
	 * Auth Service.
	 *
	 * This interceptor extracts the JWT from the authenticated Spring Security
	 * context and adds it to the Feign request.
	 */
	@Bean
	public RequestInterceptor jwtRequestInterceptor() {

		return requestTemplate -> {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication instanceof AbstractOAuth2TokenAuthenticationToken<?> tokenAuthentication) {

				String token = tokenAuthentication.getToken().getTokenValue();

				requestTemplate.header("Authorization", "Bearer " + token);
			}
		};
	}
}