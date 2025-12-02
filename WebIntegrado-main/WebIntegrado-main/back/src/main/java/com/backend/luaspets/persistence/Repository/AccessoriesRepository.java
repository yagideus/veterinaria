package com.backend.luaspets.persistence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Accessory;


public interface AccessoriesRepository extends JpaRepository<Accessory,Integer> {
    
}
