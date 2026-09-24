package com.paystream.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth

				/*
				 * Registration and login are public.
				 */
				.requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

				/*
				 * Only ADMIN can access admin endpoints.
				 */
				.requestMatchers("/api/auth/admin/**").hasRole("ADMIN")

				/*
				 * Merchant lookup requires authentication.
				 */
				.anyRequest().authenticated())

				/*
				 * PayStream is a stateless REST API.
				 */
				.csrf(csrf -> csrf.disable())

				/*
				 * Configure JWT Resource Server.
				 */
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {

		/*
		 * Read authorities from our custom JWT claim:
		 *
		 * "role": "ADMIN"
		 */
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

		grantedAuthoritiesConverter.setAuthoritiesClaimName("role");

		/*
		 * Convert:
		 *
		 * ADMIN
		 *
		 * into:
		 *
		 * ROLE_ADMIN
		 *
		 * so that hasRole("ADMIN") works.
		 */
		grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}
}