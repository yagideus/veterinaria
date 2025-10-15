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

    LocalDate expiration_date;
    LocalDate created_at;
}
