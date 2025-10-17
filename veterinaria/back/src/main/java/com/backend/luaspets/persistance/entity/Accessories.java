package com.backend.luaspets.persistance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ACCESSORY")
public class Accessories extends Product {

    public Accessories() {
        super();
    }

    public Accessories(Integer id, String name, String brand, String description, BigDecimal price,
                       Integer stock, String category, String image_url,
                       LocalDate expiration_date, LocalDate created_at) {
        super(id, name, brand, description, price, stock, category, image_url, expiration_date, created_at);
    }
}
