package com.wyssengineeringai.chatserver.filter;

import com.wyssengineeringai.chatserver.service.JwtService;
import com.wyssengineeringai.chatserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationWebFilter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationWebFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationWebFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // Skip authentication for auth endpoints
        if (path.startsWith("/api/auth/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.debug("No valid Authorization header found for path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            String username = jwtService.extractUsername(token);
            
            return userService.getUserByUsername(username)
                    .flatMap(user -> {
                        // Set userId as request attribute for controllers
                        exchange.getAttributes().put("userId", user.getId());
                        exchange.getAttributes().put("username", user.getUsername());
                        
                        // Create authentication token
                        UsernamePasswordAuthenticationToken authentication = 
                                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                        
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        logger.warn("User not found for username: {}", username);
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }));
        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
