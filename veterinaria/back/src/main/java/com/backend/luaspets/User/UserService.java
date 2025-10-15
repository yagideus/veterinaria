package com.backend.luaspets.User;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.luaspets.persistance.entity.Cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse updateUser(UserRequest userRequest) {

        User user = User.builder()
                .id(userRequest.id)
                .fullName(userRequest.getFullName())
                .dni(userRequest.getDni())
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .role(Role.valueOf(userRequest.getRole().toUpperCase())) 
                .build();

        userRepository.updateUser(user.id, user.fullName, user.dni, user.address, user.phoneNumber, user.getRole());
        return new UserResponse("El usuario se actualizó satisfactoriamente");

    }

    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            UserDTO userDTO = UserDTO.builder()
                    .id(user.id)
                    .username(user.username)
                    .fullName(user.fullName)
                    .dni(user.dni)
                    .address(user.address)
                    .phoneNumber(user.phoneNumber)
                    .role(user.getRole().name())
                    .build();
            return userDTO;
        }
        return null;
    }

    /* Nuevas implementaciones */

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getDni(), user.getFullName(),
                        user.getAddress(), user.getPhoneNumber(),  user.getRole().name()))
                .collect(Collectors.toList());
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }


    public Cart getCartByUserId(Integer userId) {
        return userRepository.findById(userId)
            .map(User::getCart)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}
