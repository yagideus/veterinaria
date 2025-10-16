package com.backend.luaspets.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                // return http
                //         .csrf(csrf -> csrf.disable())
                //         .authorizeHttpRequests(auth -> auth
                //                 .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                //                 .anyRequest().authenticated()
                //         )
                //         .formLogin(form -> form
                //                 .loginPage("/login")
                //                 .defaultSuccessUrl("/api/v1/user", true) // o la ruta que quieras
                //                 .permitAll()
                //         )
                //         .logout(logout -> logout
                //                 .logoutUrl("/logout")
                //                 .logoutSuccessUrl("/login?logout")
                //                 .permitAll()
                //         )
                //         .build();
        }

}
