package com.backend.luaspets.domain.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.persistence.Model.Food;
import com.backend.luaspets.persistence.Repository.FoodRepository;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    // Obtener todos los productos
    public List<Food> getAllFood() {
        return foodRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Food> getFoodById(Integer id) {
        return foodRepository.findById(id);
    }

    // Guardar un nuevo producto
    public Food saveFood(Food food) {
        // Eliminar la verificación de existencia por ID
        return foodRepository.save(food);
    }

    // Actualizar un producto existente
    public Food updateFood(Integer id, Food foodDetails) {
        return foodRepository.findById(id).map(foodToUpdate -> {
            foodToUpdate.setName(foodDetails.getName());
            foodToUpdate.setBrand(foodDetails.getBrand());
            foodToUpdate.setDescription(foodDetails.getDescription());
            foodToUpdate.setPrice(foodDetails.getPrice());
            foodToUpdate.setStock(foodDetails.getStock());
            foodToUpdate.setCategory(foodDetails.getCategory());
            foodToUpdate.setImage_url(foodDetails.getImage_url());
            foodToUpdate.setExpiration_date(foodDetails.getExpiration_date());
            foodToUpdate.setCreated_at(foodDetails.getCreated_at());
            return foodRepository.save(foodToUpdate);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Eliminar un producto
    public void deleteFood(Integer id) {
        if (!foodRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        foodRepository.deleteById(id);
    }
}
