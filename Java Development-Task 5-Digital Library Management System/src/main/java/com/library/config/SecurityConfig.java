package com.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final MemberStatusFilter memberStatusFilter;

    public SecurityConfig(MemberStatusFilter memberStatusFilter) {
        this.memberStatusFilter = memberStatusFilter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/",
                    "/login.html",
                    "/register.html",
                    "/forgot-password.html",
                    "/css/**",
                    "/js/**",
                    "/api/auth/**"
                ).permitAll()

                .requestMatchers(
                    "/admin/**",
                    "/api/admin/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    "/user/**",
                    "/api/user/**"
                ).hasRole("USER")

                .anyRequest().permitAll()
            )

            .exceptionHandling(ex -> ex

                .authenticationEntryPoint((req, res, authEx) -> {

                    if (req.getRequestURI().startsWith("/api/")) {
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        res.sendRedirect("/login.html");
                    }
                })

                .accessDeniedHandler((req, res, deniedEx) -> {

                    if (req.getRequestURI().startsWith("/api/")) {
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    } else {
                        res.sendRedirect("/login.html");
                    }
                })
            )

            .formLogin(form -> form.disable())

            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(
                    (req, res, auth) -> res.setStatus(204)
                )
            )

            .addFilterAfter(
                memberStatusFilter,
                SecurityContextHolderFilter.class
            );

        return http.build();
    }
}