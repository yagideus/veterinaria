package com.backend.luaspets.persistance.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    
}
