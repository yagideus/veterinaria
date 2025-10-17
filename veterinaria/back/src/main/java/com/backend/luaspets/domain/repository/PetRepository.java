package com.backend.luaspets.domain.repository;

import java.util.List;

import com.backend.luaspets.User.User;
import com.backend.luaspets.domain.DTO.PetRequest;
import com.backend.luaspets.domain.DTO.PetResponse;

public interface PetRepository {

    PetResponse save(PetRequest request, User owner);
    PetResponse getById(Integer id);
    List<PetResponse> getAll();
    List<PetResponse> getByOwnerId(Integer userId);
    PetResponse update(Integer petId, PetRequest request);
    void delete(Integer petId);
}
