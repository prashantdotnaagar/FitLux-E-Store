package com.fitlux.estore.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Jwtutil jwtUtil;

    public JwtAuthenticationFilter(Jwtutil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws java.io.IOException, jakarta.servlet.ServletException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            Claims claims = jwtUtil.extractClaims(token);

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            String role = claims.get("role", String.class);
            if (role != null) {
                authorities.add(new SimpleGrantedAuthority(role)); 
            }

            List<String> permissions = claims.get("permissions", List.class);
            if (permissions != null) {
                authorities.addAll(
                        permissions.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                );
            }

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}
