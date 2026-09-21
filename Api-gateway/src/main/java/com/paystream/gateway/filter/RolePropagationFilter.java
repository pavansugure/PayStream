package com.paystream.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Global Gateway filter responsible for propagating the authenticated user's
 * role to downstream microservices.
 *
 * Security flow:
 *
 * Client ↓ JWT ↓ Gateway validates JWT ↓ JWT "role" claim ↓ X-User-Role header
 * ↓ Downstream service
 *
 * The role is extracted only after Spring Security has authenticated the JWT.
 * Therefore, the client cannot simply choose its own role.
 */
@Component
public class RolePropagationFilter implements GlobalFilter, Ordered {

	private static final String ROLE_HEADER = "X-User-Role";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		/*
		 * Remove any X-User-Role header supplied by the client.
		 *
		 * This prevents a malicious client from sending:
		 *
		 * X-User-Role: ADMIN
		 *
		 * and pretending to be an administrator.
		 */
		ServerWebExchange sanitizedExchange = exchange.mutate()
				.request(request -> request.headers(headers -> headers.remove(ROLE_HEADER))).build();

		/*
		 * ReactiveSecurityContextHolder provides the SecurityContext created by Spring
		 * Security after successful JWT validation.
		 */
		return ReactiveSecurityContextHolder.getContext().flatMap(securityContext -> {

			var authentication = securityContext.getAuthentication();

			/*
			 * JwtAuthenticationToken contains the authenticated JWT and therefore gives us
			 * access to its claims.
			 */
			if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {

				String role = jwtAuthentication.getToken().getClaimAsString("role");

				if (role != null && !role.isBlank()) {

					ServerWebExchange exchangeWithRole = sanitizedExchange.mutate()
							.request(request -> request.headers(headers -> headers.set(ROLE_HEADER, role))).build();

					return chain.filter(exchangeWithRole);
				}
			}

			/*
			 * No role was found.
			 *
			 * Continue without creating a trusted role header.
			 */
			return chain.filter(sanitizedExchange);
		}).switchIfEmpty(chain.filter(sanitizedExchange));
	}

	/**
	 * Run the filter after Spring Security has established the authenticated
	 * SecurityContext.
	 *
	 * A lower numerical value means higher priority.
	 */
	@Override
	public int getOrder() {
		return 100;
	}
}