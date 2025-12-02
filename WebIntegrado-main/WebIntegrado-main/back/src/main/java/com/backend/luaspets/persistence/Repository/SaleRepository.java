package com.backend.luaspets.persistence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    
}
