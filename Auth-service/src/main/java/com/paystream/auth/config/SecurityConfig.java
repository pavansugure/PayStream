package com.paystream.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	/*
	 * SecurityFilterChain defines how Spring Security handles incoming HTTP
	 * requests.
	 *
	 * We are currently implementing registration, so the registration endpoint must
	 * be publicly accessible.
	 *
	 * Later, when JWT authentication is implemented, protected endpoints will
	 * require a valid JWT.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				/*
				 * Registration is a public endpoint because a user does not have a JWT before
				 * registering.
				 */
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
						.anyRequest().authenticated())

				/*
				 * We are building a stateless REST API rather than a browser-based form
				 * application.
				 *
				 * CSRF protection is primarily designed around browser session/cookie-based
				 * applications. Our later JWT-based API will authenticate using the
				 * Authorization header.
				 */
				.csrf(csrf -> csrf.disable());

		return http.build();
	}
}