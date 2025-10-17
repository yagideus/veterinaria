package com.backend.luaspets.persistance.mapper;

import org.springframework.stereotype.Component;

import com.backend.luaspets.User.User;
import com.backend.luaspets.domain.DTO.PetRequest;
import com.backend.luaspets.domain.DTO.PetResponse;
import com.backend.luaspets.persistance.entity.Pet;

@Component
public class PetMapper {

    public Pet toEntity(PetRequest request, User owner) {
        Pet pet = new Pet();
        pet.setOwner(owner); // ← aquí usamos "owner"
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setSize(request.getSize());
        pet.setWeight(request.getWeight());
        pet.setAge(request.getAge());
        pet.setGender(request.getGender());
        return pet;
    }

    public PetResponse toResponse(Pet pet) {
        PetResponse response = new PetResponse();
        response.setId(pet.getId());
        response.setUserId(pet.getOwner().getId()); // ← también aquí
        response.setUserName(pet.getOwner().getUsername());
        response.setName(pet.getName());
        response.setSpecies(pet.getSpecies());
        response.setBreed(pet.getBreed());
        response.setSize(pet.getSize());
        response.setWeight(pet.getWeight());
        response.setAge(pet.getAge());
        response.setGender(pet.getGender());
        return response;
    }
}