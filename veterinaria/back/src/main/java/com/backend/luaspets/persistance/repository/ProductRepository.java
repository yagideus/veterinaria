package com.backend.luaspets.persistance.repository;

import com.backend.luaspets.domain.DTO.ProductDTO;
import com.backend.luaspets.domain.repository.ProductDTORepository;
import com.backend.luaspets.persistance.crud.*;
import com.backend.luaspets.persistance.entity.*;
import com.backend.luaspets.persistance.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository implements ProductDTORepository {
    
    @Autowired
    private ProductCrudRepository productoCrudRepository;
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    @Autowired
    private FoodRepository foodRepository;
    
    @Autowired
    private AccessoriesRepository accessoriesRepository;

    @Autowired
    private ProductMapper mapper;

    @Override
    public List<ProductDTO> getAll(){
        List<Product> productos = (List<Product>) productoCrudRepository.findAll();
        return mapper.toProductsDTO(productos);
    }

    @Override
    public Optional<List<ProductDTO>> getByCategory(int categoryId) {
        // Convertir categoryId a String para buscar por categoría
        String category = String.valueOf(categoryId);
        List<Product> productos = productoCrudRepository.findByCategoryOrderByNameAsc(category);
        return Optional.of(mapper.toProductsDTO(productos));
    }

    @Override
    public Optional<List<ProductDTO>> getScarseProducts(int quantity) {
        List<Product> productos = productoCrudRepository.findByStockLessThan(quantity);
        return Optional.of(mapper.toProductsDTO(productos));
    }

    @Override
    public Optional<ProductDTO> getProduct(int productId) {
        return productoCrudRepository.findById(productId).map(producto -> mapper.toProductDTO(producto));
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        String productType = productDTO.getProductType();
        Product savedProduct = null;
        
        if ("medicine".equalsIgnoreCase(productType)) {
            Medicine medicine = mapper.toMedicine(productDTO);
            savedProduct = medicineRepository.save(medicine);
        } else if ("food".equalsIgnoreCase(productType)) {
            Food food = mapper.toFood(productDTO);
            savedProduct = foodRepository.save(food);
        } else if ("accessory".equalsIgnoreCase(productType)) {
            Accessories accessory = mapper.toAccessory(productDTO);
            savedProduct = accessoriesRepository.save(accessory);
        } else {
            // Si no se especifica tipo, guardar como Medicine por defecto
            Medicine medicine = mapper.toMedicine(productDTO);
            savedProduct = medicineRepository.save(medicine);
        }
        
        return mapper.toProductDTO(savedProduct);
    }

    @Override
    public void delete(int idProducto) {
        productoCrudRepository.deleteById(idProducto);
    }
    
    // Métodos adicionales para obtener productos por tipo específico
    public List<ProductDTO> getAllMedicines() {
        List<Medicine> medicines = medicineRepository.findAll();
        return mapper.toProductsDTOFromAny(medicines);
    }
    
    public List<ProductDTO> getAllFood() {
        List<Food> foods = foodRepository.findAll();
        return mapper.toProductsDTOFromAny(foods);
    }
    
    public List<ProductDTO> getAllAccessories() {
        List<Accessories> accessories = accessoriesRepository.findAll();
        return mapper.toProductsDTOFromAny(accessories);
    }
    
    public Optional<ProductDTO> getMedicineById(int id) {
        return medicineRepository.findById(id).map(mapper::medicineToProductDTO);
    }
    
    public Optional<ProductDTO> getFoodById(int id) {
        return foodRepository.findById(id).map(mapper::foodToProductDTO);
    }
    
    public Optional<ProductDTO> getAccessoryById(int id) {
        return accessoriesRepository.findById(id).map(mapper::accessoryToProductDTO);
    }
}
