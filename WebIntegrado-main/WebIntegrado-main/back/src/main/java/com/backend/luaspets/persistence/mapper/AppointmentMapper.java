package com.backend.luaspets.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.backend.luaspets.domain.DTO.AppointmentRequest;
import com.backend.luaspets.domain.DTO.AppointmentResponse;
import com.backend.luaspets.persistence.Model.Appointment;
import com.backend.luaspets.persistence.Model.Pet;
import com.backend.luaspets.persistence.Model.Services;
import com.backend.luaspets.User.User;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "userIdToUser")
    @Mapping(target = "pet", source = "petId", qualifiedByName = "petIdToPet")
    @Mapping(target = "service", source = "serviceId", qualifiedByName = "serviceIdToService")
    Appointment appointmentRequestToAppointment(AppointmentRequest appointmentRequest);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "petId", source = "pet.id")
    @Mapping(target = "serviceId", source = "service.id")
    AppointmentResponse appointmentToAppointmentResponse(Appointment appointment);

    @Named("userIdToUser")
    default User userIdToUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("petIdToPet")
    default Pet petIdToPet(Integer petId) {
        if (petId == null) {
            return null;
        }
        Pet pet = new Pet();
        pet.setId(petId);
        return pet;
    }

    @Named("serviceIdToService")
    default Services serviceIdToService(Integer serviceId) {
        if (serviceId == null) {
            return null;
        }
        Services service = new Services();
        service.setId(serviceId);
        return service;
    }
}

