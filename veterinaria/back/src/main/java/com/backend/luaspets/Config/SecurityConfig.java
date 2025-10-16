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
                                // Permitir acceso a páginas web sin autenticación
                                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                                // Permitir acceso a todos los endpoints de la API sin autenticación
                                .requestMatchers("/api/**").permitAll()
                                // Requerir autenticación para cualquier otra ruta
                                .anyRequest().authenticated()
                        )
                        .formLogin(form -> form
                                .loginPage("/login")
                                .defaultSuccessUrl("/api/v1/user", true)
                                .permitAll()
                        )
                        .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                        )
                        .build();
        }

}