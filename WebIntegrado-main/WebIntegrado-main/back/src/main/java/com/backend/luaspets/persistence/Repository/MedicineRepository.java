package com.backend.luaspets.persistence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine,Integer> {
    
}
