package com.backend.luaspets.domain.Services;

import com.backend.luaspets.domain.DTO.ProductDTO;
import com.backend.luaspets.domain.repository.ProductRepository;
import com.backend.luaspets.persistance.ProductRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDTOService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductRepositoryImpl productRepositoryImpl;

    public List<ProductDTO> getAll() {
        return productRepository.getAll();
    }

    public Optional<ProductDTO> getProduct(int productId) {
        return productRepository.getProduct(productId);
    }

    public Optional<List<ProductDTO>> getByCategory(int categoryId) {
        return productRepository.getByCategory(categoryId);
    }
    
    public Optional<List<ProductDTO>> getScarseProducts(int quantity) {
        return productRepository.getScarseProducts(quantity);
    }

    public ProductDTO save(ProductDTO product) {
        return productRepository.save(product);
    }

    public boolean delete(int productId) {
        return getProduct(productId).map(product -> {
            productRepository.delete(productId);
            return true;
        }).orElse(false);
    }
    
    // Métodos específicos para cada tipo de producto
    public List<ProductDTO> getAllMedicines() {
        return productRepositoryImpl.getAllMedicines();
    }
    
    public List<ProductDTO> getAllFood() {
        return productRepositoryImpl.getAllFood();
    }
    
    public List<ProductDTO> getAllAccessories() {
        return productRepositoryImpl.getAllAccessories();
    }
    
    public Optional<ProductDTO> getMedicineById(int id) {
        return productRepositoryImpl.getMedicineById(id);
    }
    
    public Optional<ProductDTO> getFoodById(int id) {
        return productRepositoryImpl.getFoodById(id);
    }
    
    public Optional<ProductDTO> getAccessoryById(int id) {
        return productRepositoryImpl.getAccessoryById(id);
    }

}