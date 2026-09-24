package com.paystream.auth.service;

import com.paystream.auth.dto.LoginRequest;
import com.paystream.auth.dto.LoginResponse;
import com.paystream.auth.dto.MerchantStatusResponse;
import com.paystream.auth.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    MerchantStatusResponse getMerchantStatus(Long merchantId);

    void verifyMerchant(Long merchantId);
}