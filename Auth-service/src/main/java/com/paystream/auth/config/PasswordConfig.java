package com.paystream.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

	/*
	 * BCrypt is a password-hashing algorithm designed specifically for securely
	 * storing passwords.
	 *
	 * We expose PasswordEncoder as a Spring Bean so that the service layer can
	 * receive it through constructor injection.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}