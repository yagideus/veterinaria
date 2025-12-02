package com.backend.luaspets.domain.Services;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.persistence.Model.Medicine;
import com.backend.luaspets.persistence.Repository.MedicineRepository;

@Service
public class MedicineService {
     private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    // Obtener todos los productos
    public List<Medicine> getAllMedicine() {
        return medicineRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Medicine> getMedicineById(Integer id) {
        return medicineRepository.findById(id);
    }

    // Guardar un nuevo producto
    public Medicine saveMedicine(Medicine food) {
        // Eliminar la verificación de existencia por ID
        return medicineRepository.save(food);
    }

    // Actualizar un producto existente
    public Medicine updateMedicine(Integer id, Medicine foodDetails) {
        return medicineRepository.findById(id).map(foodToUpdate -> {
            foodToUpdate.setName(foodDetails.getName());
            foodToUpdate.setBrand(foodDetails.getBrand());
            foodToUpdate.setDescription(foodDetails.getDescription());
            foodToUpdate.setPrice(foodDetails.getPrice());
            foodToUpdate.setStock(foodDetails.getStock());
            foodToUpdate.setCategory(foodDetails.getCategory());
            foodToUpdate.setImage_url(foodDetails.getImage_url());
            foodToUpdate.setExpiration_date(foodDetails.getExpiration_date());
            foodToUpdate.setCreated_at(foodDetails.getCreated_at());
            return medicineRepository.save(foodToUpdate);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Eliminar un producto
    public void deleteMedicine(Integer id) {
        if (!medicineRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        medicineRepository.deleteById(id);
    }
}
