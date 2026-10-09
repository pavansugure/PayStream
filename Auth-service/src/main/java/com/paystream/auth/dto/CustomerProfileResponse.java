package com.paystream.auth.dto;

import java.time.LocalDate;

public record CustomerProfileResponse(

		Long userId,

		String username,

		String email,

		String firstName,

		String lastName,

		String phoneNumber,

		LocalDate dateOfBirth,

		String street,

		String city,

		String state,

		String postalCode,

		String country) {
}