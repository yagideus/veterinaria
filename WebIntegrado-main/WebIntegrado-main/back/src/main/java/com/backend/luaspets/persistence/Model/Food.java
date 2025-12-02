package com.backend.luaspets.persistence.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("FOOD") // Usa mayúsculas para coincidir con la DB
@Getter
@Setter

@Builder
public class Food extends Product{
}
