package com.backend.luaspets.persistence.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer>{
    List<Pet> findAllByOwner_Id(Integer userId);
}
