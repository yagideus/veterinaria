package com.backend.luaspets.domain.Services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.persistance.crud.AccessoriesRepository;
import com.backend.luaspets.persistance.entity.Accessories;


@Service
public class AccessoriesService {

    private final AccessoriesRepository accessoriesRepository;

    @Autowired
    public AccessoriesService(AccessoriesRepository accessoriesRepository) {
        this.accessoriesRepository = accessoriesRepository;
    }

    // Obtener todos los productos
    public List<Accessories> getAllAccessories() {
        return accessoriesRepository.findAll();
    }

    // Obtener un producto por ID
    public Optional<Accessories> getAccessoriesById(Integer id) {
        return accessoriesRepository.findById(id);
    }

    // Guardar un nuevo producto
    public Accessories saveAccessories(Accessories accessories) {
        // Eliminar la verificación de existencia por ID
        return accessoriesRepository.save(accessories);
    }

    // Actualizar un producto existente
   public Accessories updateAccessories(Integer id, Accessories accessoriesDetails) {
        return accessoriesRepository.findById(id).map(accessoriesToUpdate -> {
            accessoriesToUpdate.setName(accessoriesDetails.getName());
            accessoriesToUpdate.setBrand(accessoriesDetails.getBrand());
            accessoriesToUpdate.setDescription(accessoriesDetails.getDescription());
            accessoriesToUpdate.setPrice(accessoriesDetails.getPrice());
            accessoriesToUpdate.setStock(accessoriesDetails.getStock());
            accessoriesToUpdate.setCategory(accessoriesDetails.getCategory());
            accessoriesToUpdate.setImage_url(accessoriesDetails.getImage_url());
            accessoriesToUpdate.setExpiration_date(accessoriesDetails.getExpiration_date());
            accessoriesToUpdate.setCreated_at(accessoriesDetails.getCreated_at());
            return accessoriesRepository.save(accessoriesToUpdate);
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
