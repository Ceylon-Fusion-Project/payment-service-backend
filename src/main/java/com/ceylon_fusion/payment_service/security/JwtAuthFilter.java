package com.ceylon_fusion.payment_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // Extract authorities from the token
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");

                // Create an authentication token
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), // Username
                        null, // Credentials (not needed for JWT)
                        authorities.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                );

                // Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // Clear the SecurityContext if token validation fails
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return; // Stop further processing
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Exclude public endpoints from JWT filtering
        return path.equals("/v3/api-docs") ||
                path.startsWith("/v3/api-docs/") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/swagger-ui/") ||
                path.equals("/actuator/health") ||
                path.startsWith("/api/v1/auth/") || // Exclude auth endpoints
                path.equals("/api/v1/stripe/webhook"); // Exclude Stripe webhook
    }
}
