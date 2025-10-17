package com.backend.luaspets.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                  return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 🔓 Permite todo
            )
            .formLogin(form -> form.disable()) // 🔒 Desactiva login
            .logout(logout -> logout.disable()) // 🔒 Desactiva logout
            .build();
        }

}
