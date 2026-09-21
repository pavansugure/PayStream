package com.paystream.auth.service;

import com.paystream.auth.dto.LoginRequest;
import com.paystream.auth.dto.LoginResponse;
import com.paystream.auth.dto.RegisterRequest;
import com.paystream.auth.entity.User;
import com.paystream.auth.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@Service
public class AuthServiceImpl implements AuthService {

	/*
	 * Constructor injection is preferred over field injection.
	 *
	 * The dependency is final, which means the reference cannot be reassigned after
	 * the object is created.
	 *
	 * Spring automatically provides the UserRepository implementation when it
	 * creates this service bean.
	 */
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtEncoder;

	private final long jwtExpirationMs;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
			@Value("${jwt.expiration-ms}") long jwtExpirationMs) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtEncoder = jwtEncoder;
		this.jwtExpirationMs = jwtExpirationMs;
	}

	@Override
	public void register(RegisterRequest request) {

		/*
		 * RegisterRequest is a Java 21 record. Therefore, we access its values using:
		 *
		 * request.username() request.email() request.password()
		 */
		if (userRepository.findByUsername(request.username()).isPresent()) {
			throw new IllegalArgumentException("Username already exists");
		}

		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new IllegalArgumentException("Email already exists");
		}

		/*
		 * Never store the raw password in the database.
		 *
		 * BCrypt generates a one-way password hash. During login, we will later use
		 * PasswordEncoder.matches() to compare the entered password with this stored
		 * hash.
		 */
		String encodedPassword = passwordEncoder.encode(request.password());

		/*
		 * Create the JPA entity that will be persisted to MySQL.
		 *
		 * New users receive the default application role USER.
		 */
		User user = new User(request.username(), request.email(), encodedPassword, "CUSTOMER");

		userRepository.save(user);
	}

	@Override
	public LoginResponse login(LoginRequest request) {

		/*
		 * Step 1: Find the user using the username supplied during login.
		 *
		 * Optional.orElseThrow() allows us to stop immediately when the username does
		 * not exist.
		 */
		User user = userRepository.findByUsername(request.username())
				.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

		/*
		 * Step 2: Compare the raw password entered by the user with the BCrypt hash
		 * stored in the database.
		 *
		 * PasswordEncoder.matches() performs this comparison safely.
		 *
		 * We NEVER decrypt the stored password because BCrypt is a one-way password
		 * hashing algorithm.
		 */
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {

			throw new BadCredentialsException("Invalid username or password");
		}

		/*
		 * Step 3: Capture the current time once.
		 *
		 * Using the same Instant for issuedAt and expiration calculations makes the
		 * token timestamps consistent.
		 */
		Instant now = Instant.now();

		/*
		 * Step 4: Build the JWT payload.
		 *
		 * The claims describe the identity and authorization information associated
		 * with the authenticated user.
		 */
		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("paystream-auth").subject(user.getId().toString())
				.claim("role", user.getRole()).issuedAt(now).expiresAt(now.plusMillis(jwtExpirationMs)).build();

		/*
		 * Step 5: Define the JWT signing algorithm.
		 *
		 * HS256 uses the secret key configured inside JwtConfig.
		 */
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

		/*
		 * Step 6: Combine the header and claims into the parameters required by
		 * JwtEncoder.
		 */
		JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claims);

		/*
		 * Step 7: Sign the JWT and extract the final token string.
		 */
		String accessToken = jwtEncoder.encode(parameters).getTokenValue();

		/*
		 * Step 8: Return the JWT to the client.
		 *
		 * "Bearer" tells the client that this token should later be sent using:
		 *
		 * Authorization: Bearer <token>
		 */
		return new LoginResponse(accessToken, "Bearer");
	}
}