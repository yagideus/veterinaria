package com.backend.luaspets.persistance.mapper;

import com.backend.luaspets.domain.DTO.ProductDTO;
import com.backend.luaspets.persistance.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    // Convertir Product a ProductDTO
    public ProductDTO toProductDTO(Product product) {
        if (product == null) return null;

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

        if (product instanceof Medicine) {
            dto.setProductType("medicine");
        } else if (product instanceof Food) {
            dto.setProductType("food");
        } else if (product instanceof Accessories) {
            dto.setProductType("accessory");
        }

        return dto;
    }

    // Convertir lista de Product a lista de ProductDTO
    public List<ProductDTO> toProductsDTO(List<? extends Product> productos) {
        if (productos == null) return null;
        return productos.stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }

    // Convertir ProductDTO a Product según tipo
    public Product toProduct(ProductDTO dto) {
        if (dto == null) return null;

        switch (dto.getProductType() != null ? dto.getProductType().toLowerCase() : "") {
            case "medicine":
                return buildProduct(new Medicine(), dto);
            case "food":
                return buildProduct(new Food(), dto);
            case "accessory":
                return buildProduct(new Accessories(), dto);
            default:
                return buildProduct(new Medicine(), dto); // fallback
        }
    }

    // Método genérico para poblar cualquier subclase de Product
    private <T extends Product> T buildProduct(T product, ProductDTO dto) {
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());
        product.setImage_url(dto.getImage_url());
        product.setExpiration_date(dto.getExpiration_date());
        product.setCreated_at(dto.getCreated_at());
        return product;
    }
        // Convertir lista de cualquier subtipo de Product a lista de ProductDTO
    public List<ProductDTO> toProductsDTOFromAny(List<? extends Product> productos) {
        return toProductsDTO(productos);
    }

}