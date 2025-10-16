package com.backend.luaspets.persistance.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Product;

import java.util.List;

public interface ProductCrudRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryOrderByNameAsc(String category);

    List<Product> findByStockLessThan(Integer quantity);
}
