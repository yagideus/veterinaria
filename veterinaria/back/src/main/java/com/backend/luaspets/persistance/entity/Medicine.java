package com.backend.luaspets.persistance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MEDICINE")
public class Medicine extends Product {

    public Medicine() {
        super();
    }

    public Medicine(Integer id, String name, String brand, String description, BigDecimal price,
                    Integer stock, String category, String image_url,
                    LocalDate expiration_date, LocalDate created_at) {
        super(id, name, brand, description, price, stock, category, image_url, expiration_date, created_at);
    }
}
