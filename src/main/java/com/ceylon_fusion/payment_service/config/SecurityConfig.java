package com.ceylon_fusion.payment_service.config;

import com.ceylon_fusion.payment_service.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF and enable CORS
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))

                // Set session management to stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()  // Auth endpoints public

                        // Stripe webhook endpoint
                        .requestMatchers("/api/v1/stripe/webhook").permitAll()

                        // Payment endpoints
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/payments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/payments/**").hasRole("ADMIN")

                        // Payment Methods endpoints
                        .requestMatchers(HttpMethod.POST, "/api/payment-methods/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/payment-methods/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/payment-methods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/payment-methods/**").hasRole("ADMIN")

                        // Refund endpoints
                        .requestMatchers(HttpMethod.POST, "/api/refunds/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/refunds/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/refunds/**").hasRole("ADMIN")

                        // Any other request needs authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter before the default UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
