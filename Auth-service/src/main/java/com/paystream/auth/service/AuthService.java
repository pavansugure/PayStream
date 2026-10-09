package com.paystream.auth.service;

import com.paystream.auth.dto.CustomerProfileRequest;
import com.paystream.auth.dto.CustomerProfileResponse;
import com.paystream.auth.dto.LoginRequest;
import com.paystream.auth.dto.LoginResponse;
import com.paystream.auth.dto.MerchantProfileRequest;
import com.paystream.auth.dto.MerchantProfileResponse;
import com.paystream.auth.dto.MerchantStatusResponse;
import com.paystream.auth.dto.RegisterRequest;

public interface AuthService {

	void register(RegisterRequest request);

	LoginResponse login(LoginRequest request);

	MerchantStatusResponse getMerchantStatus(Long merchantId);

	void verifyMerchant(Long merchantId);

	CustomerProfileResponse getCustomerProfile(Long userId);

	CustomerProfileResponse updateCustomerProfile(Long userId, CustomerProfileRequest request);

	MerchantProfileResponse getMerchantProfile(Long userId);

	MerchantProfileResponse updateMerchantProfile(Long userId, MerchantProfileRequest request);
}