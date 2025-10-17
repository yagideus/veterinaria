package com.backend.luaspets.persistance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Product {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String image_url;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expiration_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate created_at;

    // Constructor vacío requerido por JPA
    protected Product() {}

    // Constructor completo (opcional)
    public Product(Integer id, String name, String brand, String description, BigDecimal price,
                   Integer stock, String category, String image_url,
                   LocalDate expiration_date, LocalDate created_at) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.image_url = image_url;
        this.expiration_date = expiration_date;
        this.created_at = created_at;
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public LocalDate getExpiration_date() { return expiration_date; }
    public void setExpiration_date(LocalDate expiration_date) { this.expiration_date = expiration_date; }

    public LocalDate getCreated_at() { return created_at; }
    public void setCreated_at(LocalDate created_at) { this.created_at = created_at; }
}

