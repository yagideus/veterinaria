package com.backend.luaspets.persistence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    
}
