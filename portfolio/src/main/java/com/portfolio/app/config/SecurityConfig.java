package com.portfolio.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Allow REST APIs, contact submissions, and H2 console without CSRF interference
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/contact", "/contact/**", "/h2-console/**")
                )
                // Permit H2 console iframes
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .authorizeHttpRequests(auth -> auth
                        // Permit login page before restricting /admin/**
                        .requestMatchers("/admin/login").permitAll()
                        // Admin-only endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Public endpoints
                        .requestMatchers(
                                "/",
                                "/about",
                                "/projects/**",
                                "/resume",
                                "/resume/**",
                                "/certificates",
                                "/certificates/**",
                                "/blog/**",
                                "/contact",
                                "/api/**",
                                "/h2-console/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
