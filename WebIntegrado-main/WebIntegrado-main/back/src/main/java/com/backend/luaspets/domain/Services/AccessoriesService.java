package com.backend.luaspets.domain.Services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.persistence.Model.Accessory;
import com.backend.luaspets.persistence.Repository.AccessoriesRepository;


@Service
public class AccessoriesService {

    private final AccessoriesRepository accessoriesRepository;

    @Autowired
    public AccessoriesService(AccessoriesRepository accessoriesRepository) {
        this.accessoriesRepository = accessoriesRepository;
    }

    // Obtener todos los productos
    public List<Accessory> getAllAccessories() {
        return accessoriesRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Accessory> getAccessoriesById(Integer id) {
        return accessoriesRepository.findById(id);
    }

    // Guardar un nuevo producto
    public Accessory saveAccessories(Accessory accessory) {
        // Eliminar la verificación de existencia por ID
        return accessoriesRepository.save(accessory);
    }

    // Actualizar un producto existente
   public Accessory updateAccessories(Integer id, Accessory accessoryDetails) {
        return accessoriesRepository.findById(id).map(accessoryToUpdate -> {
            accessoryToUpdate.setName(accessoryDetails.getName());
            accessoryToUpdate.setBrand(accessoryDetails.getBrand());
            accessoryToUpdate.setDescription(accessoryDetails.getDescription());
            accessoryToUpdate.setPrice(accessoryDetails.getPrice());
            accessoryToUpdate.setStock(accessoryDetails.getStock());
            accessoryToUpdate.setCategory(accessoryDetails.getCategory());
            accessoryToUpdate.setImage_url(accessoryDetails.getImage_url());
            accessoryToUpdate.setExpiration_date(accessoryDetails.getExpiration_date());
            accessoryToUpdate.setCreated_at(accessoryDetails.getCreated_at());
            return accessoriesRepository.save(accessoryToUpdate);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Eliminar un producto
    public void deleteAccessories(Integer id) {
        if (!accessoriesRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        accessoriesRepository.deleteById(id);
    }
}
