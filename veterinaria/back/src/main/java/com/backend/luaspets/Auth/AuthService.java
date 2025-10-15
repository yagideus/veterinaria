package com.backend.luaspets.Auth;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.luaspets.Jwt.JwtService;
import com.backend.luaspets.User.Role;
import com.backend.luaspets.User.User;
import com.backend.luaspets.User.UserRepository;
import com.backend.luaspets.persistance.entity.Cart;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.getToken(user, user.getId());
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .build();
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .dni(request.getDni())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER)
                .build();

        // Carrito
        Cart cart = new Cart();
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setUser(user);
        user.setCart(cart);

        userRepository.save(user);
    }

}
