package com.paystream.auth.config;

import com.paystream.auth.Enum.AccountType;
import com.paystream.auth.entity.User;
import com.paystream.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${admin.seed.username:}")
	private String adminUsername;

	@Value("${admin.seed.email:}")
	private String adminEmail;

	@Value("${admin.seed.password:}")
	private String adminPassword;

	public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {

		if (adminUsername.isBlank() || adminEmail.isBlank() || adminPassword.isBlank()) {

			return;
		}

		if (userRepository.findByUsername(adminUsername).isPresent()) {
			return;
		}

		User admin = new User(adminUsername, adminEmail, passwordEncoder.encode(adminPassword),
				AccountType.ADMIN.name());

		userRepository.save(admin);

		System.out.println("Admin account created successfully.");
	}
}