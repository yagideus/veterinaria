package com.backend.luaspets.persistance.mapper;

import com.backend.luaspets.domain.DTO.ProductDTO;
import com.backend.luaspets.persistance.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    // Método para convertir Product a ProductDTO
    public ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategory(product.getCategory());
        dto.setImage_url(product.getImage_url());
        dto.setExpiration_date(product.getExpiration_date());
        dto.setCreated_at(product.getCreated_at());
        
        // Determinar el tipo de producto basado en la clase
        if (product instanceof Medicine) {
            dto.setProductType("medicine");
        } else if (product instanceof Food) {
            dto.setProductType("food");
        } else if (product instanceof Accessories) {
            dto.setProductType("accessory");
        }
        
        return dto;
    }

    // Método para convertir lista de Product a ProductDTO
    public List<ProductDTO> toProductsDTO(List<Product> productos) {
        if (productos == null) {
            return null;
        }
        return productos.stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }

    // Método para convertir ProductDTO a Product específico según el tipo
    public Product toProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        
        String productType = productDTO.getProductType();
        
        if ("medicine".equalsIgnoreCase(productType)) {
            return toMedicine(productDTO);
        } else if ("food".equalsIgnoreCase(productType)) {
            return toFood(productDTO);
        } else if ("accessory".equalsIgnoreCase(productType)) {
            return toAccessory(productDTO);
        }
        
        // Si no se especifica tipo, crear un Product genérico
        return toGenericProduct(productDTO);
    }

    // Método para convertir ProductDTO a Medicine
    public Medicine toMedicine(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        
        Medicine medicine = new Medicine();
        medicine.setId(productDTO.getId());
        medicine.setName(productDTO.getName());
        medicine.setBrand(productDTO.getBrand());
        medicine.setDescription(productDTO.getDescription());
        medicine.setPrice(productDTO.getPrice());
        medicine.setStock(productDTO.getStock());
        medicine.setCategory(productDTO.getCategory());
        medicine.setImage_url(productDTO.getImage_url());
        medicine.setExpiration_date(productDTO.getExpiration_date());
        medicine.setCreated_at(productDTO.getCreated_at());
        
        return medicine;
    }

    // Método para convertir ProductDTO a Food
    public Food toFood(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        
        Food food = new Food();
        food.setId(productDTO.getId());
        food.setName(productDTO.getName());
        food.setBrand(productDTO.getBrand());
        food.setDescription(productDTO.getDescription());
        food.setPrice(productDTO.getPrice());
        food.setStock(productDTO.getStock());
        food.setCategory(productDTO.getCategory());
        food.setImage_url(productDTO.getImage_url());
        food.setExpiration_date(productDTO.getExpiration_date());
        food.setCreated_at(productDTO.getCreated_at());
        
        return food;
    }

    // Método para convertir ProductDTO a Accessories
    public Accessories toAccessory(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        
        Accessories accessory = new Accessories();
        accessory.setId(productDTO.getId());
        accessory.setName(productDTO.getName());
        accessory.setBrand(productDTO.getBrand());
        accessory.setDescription(productDTO.getDescription());
        accessory.setPrice(productDTO.getPrice());
        accessory.setStock(productDTO.getStock());
        accessory.setCategory(productDTO.getCategory());
        accessory.setImage_url(productDTO.getImage_url());
        accessory.setExpiration_date(productDTO.getExpiration_date());
        accessory.setCreated_at(productDTO.getCreated_at());
        
        return accessory;
    }

    // Método para convertir ProductDTO a Product genérico
    private Product toGenericProduct(ProductDTO productDTO) {
        // Como Product es abstracto, crearemos un Medicine por defecto
        return toMedicine(productDTO);
    }

    // Métodos específicos para cada tipo de producto
    public ProductDTO medicineToProductDTO(Medicine medicine) {
        ProductDTO dto = toProductDTO(medicine);
        if (dto != null) {
            dto.setProductType("medicine");
        }
        return dto;
    }

    public ProductDTO foodToProductDTO(Food food) {
        ProductDTO dto = toProductDTO(food);
        if (dto != null) {
            dto.setProductType("food");
        }
        return dto;
    }

    public ProductDTO accessoryToProductDTO(Accessories accessory) {
        ProductDTO dto = toProductDTO(accessory);
        if (dto != null) {
            dto.setProductType("accessory");
        }
        return dto;
    }

    // Método para convertir cualquier Product a ProductDTO según su tipo
    public ProductDTO toProductDTOFromAny(Product product) {
        return toProductDTO(product);
    }

    // Método para convertir una lista de cualquier tipo de Product a ProductDTO
    public List<ProductDTO> toProductsDTOFromAny(List<? extends Product> productos) {
        if (productos == null) {
            return null;
        }
        return productos.stream()
                .map(this::toProductDTOFromAny)
                .collect(Collectors.toList());
    }
}