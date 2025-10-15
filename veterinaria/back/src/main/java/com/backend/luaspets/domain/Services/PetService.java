package com.backend.luaspets.domain.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.User.User;
import com.backend.luaspets.User.UserRepository;
import com.backend.luaspets.domain.DTO.PetRequest;
import com.backend.luaspets.domain.DTO.PetResponse;
import com.backend.luaspets.persistance.crud.PetRepository;
import com.backend.luaspets.persistance.entity.Pet;

@Service
public class PetService {
    
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Pet> getAllPets(){
        return petRepository.findAll();
    }

    public PetResponse getPetById(Integer id){
        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));
        PetResponse dto = new PetResponse();
        dto.setId(pet.getId());
        dto.setUserId(pet.getOwner().getId());
        dto.setUserName(pet.getOwner().getFullName());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setBreed(pet.getBreed());
        dto.setSize(pet.getSize());
        dto.setWeight(pet.getWeight());
        dto.setAge(pet.getAge());
        dto.setGender(pet.getGender());

        return dto;
    }

    public Pet createPet(PetRequest request){
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = new Pet();
        pet.setOwner(user);
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setSize(request.getSize());
        pet.setWeight(request.getWeight());
        pet.setAge(request.getAge());
        pet.setGender(request.getGender());

        petRepository.save(pet);

        return pet;
    }

    // Obtener todas las mascotas de un usuario
    public List<Pet> getPetsByUserId(Integer userId) {
        return petRepository.findAllByOwner_Id(userId);
    }

      // Actualizar los campos de una mascota por su ID
      public Pet updatePet(Integer petId, PetRequest request) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setSize(request.getSize());
        pet.setWeight(request.getWeight());
        pet.setAge(request.getAge());
        pet.setGender(request.getGender());

        return petRepository.save(pet);
    }

    // Eliminar una mascota por su ID
    public void deletePetById(Integer petId) {
        if (!petRepository.existsById(petId)) {
            throw new RuntimeException("Pet not found");
        }
        petRepository.deleteById(petId);
    }


}
