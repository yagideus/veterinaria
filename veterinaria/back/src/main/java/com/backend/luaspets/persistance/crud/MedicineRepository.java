package com.backend.luaspets.persistance.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine,Integer> {
    
}
