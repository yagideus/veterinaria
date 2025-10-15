package com.backend.luaspets.persistance.crud;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.luaspets.persistance.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository <Appointment, Integer> {
    
    // Buscar todas las citas de un usuario específico
    List<Appointment> findByUserId(Integer userId);
    
    // Buscar citas por fecha y hora específica
    Optional<Appointment> findByAppointmentDateAndStartTimeAndEndTime(
            LocalDateTime appointmentDate, LocalDateTime startTime, LocalDateTime endTime);
}
