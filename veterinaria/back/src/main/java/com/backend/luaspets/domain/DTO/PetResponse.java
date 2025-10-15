package com.backend.luaspets.domain.DTO;

import lombok.Data;

@Data
public class PetResponse {
    
    Integer id;
    Integer userId;
    String userName;
    String name;
    String species;
    String breed;
    String size;
    Double weight;
    Integer age;
    String gender;

}
