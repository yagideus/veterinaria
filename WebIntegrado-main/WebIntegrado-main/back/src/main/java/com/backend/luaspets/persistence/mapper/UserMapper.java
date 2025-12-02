package com.backend.luaspets.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.backend.luaspets.User.User;
import com.backend.luaspets.User.UserDTO;
import com.backend.luaspets.User.UserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    User userRequestToUser(UserRequest userRequest);

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    UserDTO userToUserDTO(User user);

    @Named("stringToRole")
    default com.backend.luaspets.User.Role stringToRole(String role) {
        if (role == null) {
            return null;
        }
        try {
            return com.backend.luaspets.User.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Named("roleToString")
    default String roleToString(com.backend.luaspets.User.Role role) {
        if (role == null) {
            return null;
        }
        return role.name();
    }
}

