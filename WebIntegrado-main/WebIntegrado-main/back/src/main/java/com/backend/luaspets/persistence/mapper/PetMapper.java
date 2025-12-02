package com.backend.luaspets.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.backend.luaspets.domain.DTO.PetRequest;
import com.backend.luaspets.domain.DTO.PetResponse;
import com.backend.luaspets.persistence.Model.Pet;
import com.backend.luaspets.User.User;

@Mapper(componentModel = "spring")
public interface PetMapper {
    
    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "userId", qualifiedByName = "userIdToUser")
    Pet petRequestToPet(PetRequest petRequest);

    @Mapping(target = "userId", source = "owner.id")
    @Mapping(target = "userName", source = "owner.username")
    PetResponse petToPetResponse(Pet pet);

    @Named("userIdToUser")
    default User userIdToUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}

