package com.backend.luaspets.persistance.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Food;

/* El Jpa Crea los metodos para hacer el CRUD */
public interface FoodRepository extends JpaRepository<Food,Integer> {

    
}
