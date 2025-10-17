package com.backend.luaspets.domain.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.User.User;
import com.backend.luaspets.User.UserRepository;
import com.backend.luaspets.domain.DTO.PetRequest;
import com.backend.luaspets.domain.DTO.PetResponse;
import com.backend.luaspets.domain.repository.PetRepository;


@Service
public class PetService {
    
 @Autowired private PetRepository petRepository;
    @Autowired private UserRepository userRepository;

    public List<PetResponse> getAllPets() {
        return petRepository.getAll();
    }

    public PetResponse getPetById(Integer id) {
        return petRepository.getById(id);
    }

    public PetResponse createPet(PetRequest request) {
        User owner = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        return petRepository.save(request, owner);
    }

    public List<PetResponse> getPetsByUserId(Integer userId) {
        return petRepository.getByOwnerId(userId);
    }

    public PetResponse updatePet(Integer petId, PetRequest request) {
        return petRepository.update(petId, request);
    }

    public void deletePetById(Integer petId) {
        petRepository.delete(petId);
    }

}
