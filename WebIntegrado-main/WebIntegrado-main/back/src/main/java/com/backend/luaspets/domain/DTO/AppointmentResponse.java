package com.backend.luaspets.domain.DTO;


import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AppointmentResponse {
    
    Integer id;
    Integer userId;
    Integer serviceId;
    Integer petId;
    LocalDate appointmentDate;
    LocalTime startTime;
    LocalTime endTime;
    String status;


}
