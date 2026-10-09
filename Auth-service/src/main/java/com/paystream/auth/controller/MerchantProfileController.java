package com.paystream.auth.controller;

import com.paystream.auth.dto.MerchantProfileRequest;
import com.paystream.auth.dto.MerchantProfileResponse;
import com.paystream.auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants/me")
public class MerchantProfileController {

	private final AuthService authService;

	public MerchantProfileController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping
	@PreAuthorize("hasRole('MERCHANT')")
	public MerchantProfileResponse getMyProfile(@RequestHeader("X-User-Id") String userId) {

		return authService.getMerchantProfile(Long.valueOf(userId));
	}

	@PutMapping
	@PreAuthorize("hasRole('MERCHANT')")
	public ResponseEntity<MerchantProfileResponse> updateMyProfile(@RequestHeader("X-User-Id") String userId,
			@Valid @RequestBody MerchantProfileRequest request) {

		return ResponseEntity.ok(authService.updateMerchantProfile(Long.valueOf(userId), request));
	}
}