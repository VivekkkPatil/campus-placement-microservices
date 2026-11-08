package com.vivek.placement.api_gateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // Public paths
        if (path.startsWith("/auth/") || path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        String header = request.getHeaders().getFirst("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return reject(exchange, HttpStatus.UNAUTHORIZED, "Missing or malformed Authorization header");
        }

        Claims claims;
        try {
            claims = jwtService.parse(header.substring(7));
        } catch (Exception e) {
            return reject(exchange, HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        String user = claims.getSubject();
        String role = String.valueOf(claims.get("role"));

        if (!isAllowed(request.getMethod().name(), path, role)) {
            return reject(exchange, HttpStatus.FORBIDDEN, "Role " + role + " is not allowed to do this");
        }

        // Pass the verified identity to downstream services, replacing anything the client sent
        ServerHttpRequest mutated = request.mutate()
                .headers(h -> {
                    h.remove("X-User");
                    h.remove("X-Role");
                    h.add("X-User", user);
                    h.add("X-Role", role);
                })
                .build();
        return chain.filter(exchange.mutate().request(mutated).build());
    }

    /** Reads are open to any logged-in user. Writes need TPO, except students applying. */
    private boolean isAllowed(String method, String path, String role) {
        if (method.equals("GET") || method.equals("HEAD") || method.equals("OPTIONS")) return true;
        if ("TPO".equals(role)) return true;
        return "STUDENT".equals(role) && method.equals("POST") && path.equals("/applications");
    }

    private Mono<Void> reject(ServerWebExchange exchange, HttpStatus status, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] body = ("{\"status\":" + status.value() + ",\"detail\":\"" + message + "\"}")
                .getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body)));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
