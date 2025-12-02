package com.backend.luaspets.persistence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Food;

/* El Jpa Crea los metodos para hacer el CRUD */
public interface FoodRepository extends JpaRepository<Food,Integer> {

    
}
