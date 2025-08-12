package org.example.gatewayserver.config.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class JwtRolesFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange.getPrincipal()
                .filter(principal -> principal instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .map(authentication -> {
                    List<String> roles = authentication.getAuthorities().stream()
                            .map(Objects::toString)
                            .toList();
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Roles", String.join(",", roles))
                            .header("X-User-Name", authentication.getName())
                            .build();

                    return exchange.mutate().request(request).build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }
}
