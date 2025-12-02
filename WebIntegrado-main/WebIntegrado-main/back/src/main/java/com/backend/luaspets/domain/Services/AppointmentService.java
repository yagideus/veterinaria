package com.backend.luaspets.domain.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.domain.DTO.AppointmentRequest;
import com.backend.luaspets.domain.DTO.AppointmentResponse;
import com.backend.luaspets.persistence.Model.Appointment;
import com.backend.luaspets.persistence.Model.Pet;
import com.backend.luaspets.persistence.Model.Services;
import com.backend.luaspets.persistence.Repository.AppointmentRepository;
import com.backend.luaspets.persistence.Repository.PetRepository;
import com.backend.luaspets.persistence.Repository.ServiceRepository;
import com.backend.luaspets.User.User;
import com.backend.luaspets.User.UserRepository;
import com.backend.luaspets.persistence.mapper.AppointmentMapper;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;


    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public AppointmentResponse getAppoinmentById(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return appointmentMapper.appointmentToAppointmentResponse(appointment);
    }

    public Appointment createAppointment(AppointmentRequest request){
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        Services service = serviceRepository.findById(request.getServiceId())
            .orElseThrow(()-> new RuntimeException("Service not found"));
        Pet pet = petRepository.findById(request.getPetId())
            .orElseThrow(()-> new RuntimeException("Pet not found"));

        Appointment appointment = appointmentMapper.appointmentRequestToAppointment(request);
        appointment.setUser(user);
        appointment.setPet(pet);
        appointment.setService(service);
        appointment.setStatus("PENDIENTE");

        appointmentRepository.save(appointment);
        return appointment;
    }

    public Appointment updateAppointment(Integer id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        Services service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));
    
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));
    
        // Actualiza los campos de la cita
        appointment.setUser(user);
        appointment.setPet(pet);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setStatus(request.getStatus());
        appointment.setService(service);

        // Guarda la cita actualizada
        return appointmentRepository.save(appointment);
    }
    
    public void deleteAppointmentById(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        appointmentRepository.delete(appointment);
    }

}