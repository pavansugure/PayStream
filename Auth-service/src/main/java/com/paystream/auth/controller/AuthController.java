package com.paystream.auth.controller;

import com.paystream.auth.dto.LoginRequest;
import com.paystream.auth.dto.LoginResponse;
import com.paystream.auth.dto.MerchantStatusResponse;
import com.paystream.auth.dto.RegisterRequest;
import com.paystream.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	/*
	 * Constructor injection keeps the controller loosely coupled to the AuthService
	 * interface rather than AuthServiceImpl.
	 */
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	/*
	 * @Valid triggers the validation annotations defined inside the Java 21
	 * RegisterRequest record.
	 */

	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

		authService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * Authenticates the user and returns a signed JWT.
	 *
	 * The controller only handles HTTP-level responsibilities. Authentication and
	 * JWT generation remain inside the service layer.
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

		LoginResponse response = authService.login(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/merchants/{merchantId}")
	public MerchantStatusResponse getMerchantStatus(@PathVariable Long merchantId) {

		return authService.getMerchantStatus(merchantId);
	}

	@PostMapping("/admin/merchants/{merchantId}/verify")
	public ResponseEntity<Void> verifyMerchant(@PathVariable Long merchantId) {

		authService.verifyMerchant(merchantId);

		return ResponseEntity.ok().build();
	}
}