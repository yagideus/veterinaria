package com.backend.luaspets.persistance.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Accessories;


public interface AccessoriesRepository extends JpaRepository<Accessories,Integer> {
    
}
