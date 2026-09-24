package com.paystream.auth.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/*
 * RegisterRequest is a Java 21 sealed interface.
 *
 * Public registration has exactly two supported account types:
 *
 * CUSTOMER
 * MERCHANT
 *
 * ADMIN is intentionally not represented here.
 *
 * Jackson uses the "accountType" property from the JSON request
 * to determine which concrete request record should be created.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "accountType",
        visible = false
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = CustomerRegisterRequest.class,
                name = "CUSTOMER"
        ),
        @JsonSubTypes.Type(
                value = MerchantRegisterRequest.class,
                name = "MERCHANT"
        )
})
public sealed interface RegisterRequest
        permits CustomerRegisterRequest, MerchantRegisterRequest {

    String username();

    String email();

    String password();
}