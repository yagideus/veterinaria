package com.backend.luaspets.domain.Services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.persistance.crud.ServiceRepository;
import com.backend.luaspets.persistance.entity.Services;

@Service
public class ServiceService {
    
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    public List<Services> getAllServices(){
        return serviceRepository.findAll();
    }

    public Optional<Services> getServicesById(Integer id){
        return serviceRepository.findById(id);
    }

    public Services saveServices(Services services) { 
        return serviceRepository.save(services);

    }

    public Services updateServices(Integer id, Services serviceDetails){
        return serviceRepository.findById(id).map(servicesToUpdate -> {
            servicesToUpdate.setName(serviceDetails.getName());
            servicesToUpdate.setCost(serviceDetails.getCost());
            servicesToUpdate.setPetSize(serviceDetails.getPetSize());

            return serviceRepository.save(servicesToUpdate);
        }).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

    public void deleteServices(Integer id){
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado");
        }
        serviceRepository.deleteById(id);
    }

}
