package com.backend.luaspets.persistance.entity;

import com.backend.luaspets.User.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private String breed; 

    @Column(nullable = false)
    private String size; 

    @Column(nullable = false)
    private Double weight; 

    @Column(nullable = false)
    private Integer age; 

    @Column(nullable = false)
    private String gender; 

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
