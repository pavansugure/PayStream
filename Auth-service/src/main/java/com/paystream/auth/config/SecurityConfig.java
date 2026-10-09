package com.paystream.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth

				// Public endpoints
				.requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

				// Only ADMIN can verify merchants
				.requestMatchers("/api/auth/admin/**").hasRole("ADMIN")
				
				.requestMatchers("/api/auth/profile").hasRole("CUSTOMER")

				// All remaining endpoints require authentication
				.anyRequest().authenticated())

				.csrf(csrf -> csrf.disable())

				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {

		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

		converter.setJwtGrantedAuthoritiesConverter(jwt -> {

			String role = jwt.getClaimAsString("role");

			if (role == null || role.isBlank()) {
				return java.util.List.of();
			}

			return java.util.List
					.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role));
		});

		return converter;
	}
}