package com.paystream.transaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for Transaction Service.
 *
 * Authentication: - JWT is validated by Spring Security Resource Server.
 *
 * Authorization: - Roles from the JWT are converted into Spring Security
 * authorities such as ROLE_CUSTOMER, ROLE_MERCHANT and ROLE_ADMIN.
 *
 * Method-level authorization is enabled so that transaction APIs can
 * use @PreAuthorize.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				/*
				 * PayStream APIs are stateless and use JWTs instead of browser sessions.
				 */
				.csrf(csrf -> csrf.disable())

				/*
				 * Every Transaction Service endpoint requires authentication.
				 */
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())

				/*
				 * Configure Transaction Service as a JWT Resource Server.
				 */
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		return http.build();
	}

	/**
	 * Converts the custom "role" JWT claim into a Spring Security role authority.
	 *
	 * Example:
	 *
	 * JWT: "role": "CUSTOMER"
	 *
	 * becomes:
	 *
	 * ROLE_CUSTOMER
	 *
	 * This allows:
	 *
	 * @PreAuthorize("hasRole('CUSTOMER')")
	 */
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {

		JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

		/*
		 * Our JWT uses a custom claim named "role".
		 */
		authoritiesConverter.setAuthoritiesClaimName("role");

		/*
		 * Spring Security will create:
		 *
		 * ROLE_CUSTOMER ROLE_MERCHANT ROLE_ADMIN
		 */
		authoritiesConverter.setAuthorityPrefix("ROLE_");

		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

		converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

		return converter;
	}
}