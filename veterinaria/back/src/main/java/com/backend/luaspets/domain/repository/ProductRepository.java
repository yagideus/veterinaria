package com.backend.luaspets.domain.repository;

import com.backend.luaspets.domain.DTO.ProductDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    List<ProductDTO> getAll();
    Optional<List<ProductDTO>> getByCategory(int categoryId);
    Optional<List<ProductDTO>> getScarseProducts(int quantity);
    Optional<ProductDTO> getProduct(int productId);
    ProductDTO save(ProductDTO product);
    void delete(int productId);

}
