package com.backend.luaspets.persistance.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Sale;

public interface SaleCrudRepository extends JpaRepository<Sale, Integer> {
    
}
