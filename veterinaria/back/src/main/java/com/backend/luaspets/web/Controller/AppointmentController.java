package com.backend.luaspets.web.Controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.luaspets.domain.DTO.AppointmentRequest;
import com.backend.luaspets.domain.DTO.AppointmentResponse;
import com.backend.luaspets.domain.Services.AppointmentService;
import com.backend.luaspets.persistance.entity.Appointment;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {

   @Autowired
   private AppointmentService appointmentService;


   // Obtener todas las citas
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // Obtener una cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Integer id) {
        AppointmentResponse response = appointmentService.getAppoinmentById(id);
        return ResponseEntity.ok(response);
    }

   @PostMapping("/create")
   public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest request){
    
    AppointmentResponse response = new AppointmentResponse();
    Appointment appointment = appointmentService.createAppointment(request);

    response.setId(appointment.getId());
    response.setUserId(request.getUserId());
    response.setServiceId(request.getServiceId());
    response.setPetId(request.getPetId());
    response.setAppointmentDate(request.getAppointmentDate());
    response.setStartTime(request.getStartTime());
    response.setEndTime(request.getEndTime());


    return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Integer id,
            @RequestBody AppointmentRequest request) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.noContent().build();
    }

}
