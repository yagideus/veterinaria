package com.backend.luaspets.domain.DTO;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProductDTO {
    private Integer id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String image_url;

    private LocalDate expiration_date;
    private LocalDate created_at;
    private String productType; // MEDICINE, FOOD, or ACCESSORY

    // Additional fields for all product types
    private String laboratory;  // For medicine
    private String flavor;      // For food
    private String material;    // For accessories
    private String size;        // For accessories
    private Double weight;      // For food
}
