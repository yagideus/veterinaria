package com.backend.luaspets.persistance.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer>{
    List<Pet> findAllByOwner_Id(Integer userId);
}
