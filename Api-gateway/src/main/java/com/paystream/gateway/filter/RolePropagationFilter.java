package com.paystream.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class RolePropagationFilter implements GlobalFilter, Ordered {

	private static final String ROLE_HEADER = "X-User-Role";
	private static final String USER_ID_HEADER = "X-User-Id";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		/*
		 * Remove client-supplied identity headers first.
		 *
		 * This prevents a malicious client from sending:
		 *
		 * X-User-Id: another-user-id X-User-Role: ADMIN
		 *
		 * and trying to impersonate another user.
		 */
		ServerWebExchange sanitizedExchange = exchange.mutate().request(request -> request.headers(headers -> {
			headers.remove(ROLE_HEADER);
			headers.remove(USER_ID_HEADER);
		})).build();

		return ReactiveSecurityContextHolder.getContext().flatMap(securityContext -> {

			var authentication = securityContext.getAuthentication();

			if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {

				String role = jwtAuthentication.getToken().getClaimAsString("role");

				String userId = jwtAuthentication.getToken().getSubject();

				ServerWebExchange exchangeWithIdentity = sanitizedExchange.mutate()
						.request(request -> request.headers(headers -> {

							if (role != null && !role.isBlank()) {
								headers.set(ROLE_HEADER, role);
							}

							if (userId != null && !userId.isBlank()) {
								headers.set(USER_ID_HEADER, userId);
							}
						})).build();

				return chain.filter(exchangeWithIdentity);
			}

			return chain.filter(sanitizedExchange);
		}).switchIfEmpty(chain.filter(sanitizedExchange));
	}

	@Override
	public int getOrder() {
		return 100;
	}
}