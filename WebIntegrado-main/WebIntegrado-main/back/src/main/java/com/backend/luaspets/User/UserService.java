package com.backend.luaspets.User;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.luaspets.persistence.Model.Cart;
import com.backend.luaspets.persistence.mapper.UserMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse updateUser(UserRequest userRequest) {
        User user = userMapper.userRequestToUser(userRequest);
        userRepository.updateUser(user.id, user.fullName, user.dni, user.address, user.phoneNumber, user.getRole());
        return new UserResponse("El usuario se actualizó satisfactoriamente");
    }

    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return userMapper.userToUserDTO(user);
        }
        return null;
    }

    /* Nuevas implementaciones */

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Cart getCartByUserId(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Cart cart = user.getCart();
        if (cart != null && cart.getCartItems() != null) {
            cart.getCartItems().size(); // Forzar la carga de la colección lazy
        }
        return cart;
    }

}
