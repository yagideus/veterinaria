package com.backend.luaspets.persistance;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.backend.luaspets.User.User;
import com.backend.luaspets.domain.DTO.PetRequest;
import com.backend.luaspets.domain.DTO.PetResponse;
import com.backend.luaspets.domain.repository.PetRepository;
import com.backend.luaspets.persistance.crud.PetCrudRepository;
import com.backend.luaspets.persistance.entity.Pet;
import com.backend.luaspets.persistance.mapper.PetMapper;

@Repository
public class PetRepositoryImpl implements PetRepository{

     @Autowired private PetCrudRepository crud;
    @Autowired private PetMapper mapper;

    @Override
    public PetResponse save(PetRequest request, User owner) {
        Pet pet = mapper.toEntity(request, owner);
        Pet saved = crud.save(pet);
        return mapper.toResponse(saved);
    }

    @Override
    public PetResponse getById(Integer id) {
        Pet pet = crud.findById(id).orElseThrow(() -> new RuntimeException("Pet not found"));
        return mapper.toResponse(pet);
    }

    @Override
    public List<PetResponse> getAll() {
        return crud.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PetResponse> getByOwnerId(Integer userId) {
        return crud.findAllByOwner_Id(userId).stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PetResponse update(Integer petId, PetRequest request) {
        Pet pet = crud.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setSize(request.getSize());
        pet.setWeight(request.getWeight());
        pet.setAge(request.getAge());
        pet.setGender(request.getGender());

        Pet updated = crud.save(pet);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Integer petId) {
        if (!crud.existsById(petId)) {
            throw new RuntimeException("Pet not found");
        }
        crud.deleteById(petId);
    }
}

