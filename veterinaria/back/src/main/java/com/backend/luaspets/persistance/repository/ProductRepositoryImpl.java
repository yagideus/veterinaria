package com.backend.luaspets.persistance.repository;

import com.backend.luaspets.domain.DTO.ProductDTO;
import com.backend.luaspets.domain.repository.ProductRepository;
import com.backend.luaspets.persistance.crud.*;
import com.backend.luaspets.persistance.entity.*;
import com.backend.luaspets.persistance.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
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
    public List<ProductDTO> getAll() {
        List<Product> productos = (List<Product>) productoCrudRepository.findAll();
        return mapper.toProductsDTO(productos);
    }

    @Override
    public Optional<List<ProductDTO>> getByCategory(int categoryId) {
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
        return productoCrudRepository.findById(productId)
                .map(mapper::toProductDTO);
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = mapper.toProduct(productDTO);
        Product savedProduct;

        if (product instanceof Medicine) {
            savedProduct = medicineRepository.save((Medicine) product);
        } else if (product instanceof Food) {
            savedProduct = foodRepository.save((Food) product);
        } else if (product instanceof Accessories) {
            savedProduct = accessoriesRepository.save((Accessories) product);
        } else {
            // fallback defensivo
            savedProduct = productoCrudRepository.save(product);
        }

        return mapper.toProductDTO(savedProduct);
    }

    @Override
    public void delete(int idProducto) {
        productoCrudRepository.deleteById(idProducto);
    }

    // Métodos adicionales por tipo
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
        return medicineRepository.findById(id)
                .map(mapper::toProductDTO);
    }

    public Optional<ProductDTO> getFoodById(int id) {
        return foodRepository.findById(id)
                .map(mapper::toProductDTO);
    }

    public Optional<ProductDTO> getAccessoryById(int id) {
        return accessoriesRepository.findById(id)
                .map(mapper::toProductDTO);
    }
}
