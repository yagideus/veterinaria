package com.backend.luaspets.persistance.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@DiscriminatorValue("food") // Valor que se usará para diferenciar el tipo de producto en la tabla
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Food extends Product{
}
