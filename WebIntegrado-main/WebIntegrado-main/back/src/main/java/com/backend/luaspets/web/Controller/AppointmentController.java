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
import com.backend.luaspets.persistence.Model.Appointment;
import com.backend.luaspets.domain.Services.AppointmentService;
import com.backend.luaspets.persistence.mapper.AppointmentMapper;


@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = { "http://localhost:4200" })
public class AppointmentController {

   @Autowired
   private AppointmentService appointmentService;

   @Autowired
   private AppointmentMapper appointmentMapper;


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
    Appointment appointment = appointmentService.createAppointment(request);
    AppointmentResponse response = appointmentMapper.appointmentToAppointmentResponse(appointment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable Integer id,
            @RequestBody AppointmentRequest request) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, request);
        AppointmentResponse response = appointmentMapper.appointmentToAppointmentResponse(updatedAppointment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.noContent().build();
    }

 /* excel */
    /* excel */
@GetMapping("/export.xlsx")
public ResponseEntity<byte[]> exportFoodDXD() {
    try (Workbook workbook = new XSSFWorkbook()) {
        // Creación de la hoja de trabajo llamada "Citas"
        Sheet sheet = workbook.createSheet("Citas");

        // Título de la hoja
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Lista de Citas");

        // Creación y estilo del título
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14); // Ajuste de tamaño de fuente
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex()); // Color de fondo dorado
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Patrón de relleno sólido

        // Aplicación del estilo al título y fusión de celdas para centrar el texto
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7)); // Fusión de celdas desde la columna 0 a la 7

        // Creación del estilo de encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true); // Negrita para los encabezados
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior fino
        headerStyle.setBorderTop(BorderStyle.THIN); // Borde superior fino
        headerStyle.setBorderRight(BorderStyle.THIN); // Borde derecho fino
        headerStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo fino

        // Creación del estilo de celdas de datos generales
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT); // Alineación a la izquierda
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);

        // Estilo específico para celdas de precios con formato de moneda
        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.cloneStyleFrom(dataStyle); // Clona el estilo de datos generales
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00")); // Formato de moneda

        // Encabezados de la tabla
        String[] headers = {"ID", "ID Usuario", "Servicio", "Mascota", "Fecha", "Hora inicio", "Hora fin", "Estado"};
        Row headerRow = sheet.createRow(1); // Los encabezados se colocan en la fila 1, justo después del título
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle); // Aplicación del estilo de encabezado
            sheet.autoSizeColumn(i); // Ajuste automático de ancho de columna
        }

        // Obtención de los datos de las citas desde el servicio
        List<Appointment> appointments = appointmentService.getAllAppointments();
        int rowIndex = 2; // Comienza en la fila 2, después del título y los encabezados
        for (Appointment appointment : appointments) {
            Row row = sheet.createRow(rowIndex++); // Se crea una nueva fila por cada cita

            // Columna ID
            Cell cellId = row.createCell(0);
            cellId.setCellValue(appointment.getId());
            cellId.setCellStyle(dataStyle);

            // Columna ID Usuario
            Cell cellName = row.createCell(1);
            cellName.setCellValue(appointment.getUser().getId());
            cellName.setCellStyle(dataStyle);

            // Columna Servicio
            Cell cellBrand = row.createCell(2);
            cellBrand.setCellValue(appointment.getService().getName());
            cellBrand.setCellStyle(dataStyle);

            // Columna Mascota
            Cell cellDescription = row.createCell(3);
            cellDescription.setCellValue(appointment.getPet().getName());
            cellDescription.setCellStyle(dataStyle);

            // Columna Fecha (formateada como moneda, si es necesario)
            Cell cellPrice = row.createCell(4);
            cellPrice.setCellValue(appointment.getAppointmentDate());
            cellPrice.setCellStyle(currencyStyle);

            // Columna Hora inicio
            Cell cellStock = row.createCell(5);
            cellStock.setCellValue(appointment.getStartTime().toString());
            cellStock.setCellStyle(dataStyle);

            // Columna Hora fin
            Cell cellCategory = row.createCell(6);
            cellCategory.setCellValue(appointment.getEndTime().toString());
            cellCategory.setCellStyle(dataStyle);

            // Columna Estado
            Cell cellImageUrl = row.createCell(7);
            cellImageUrl.setCellValue(appointment.getStatus());
            cellImageUrl.setCellStyle(dataStyle);
        }

        // Ajuste del ancho de las columnas para evitar que el contenido quede cortado
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000); // Añade espacio adicional al ancho de la columna
        }

        // Conversión del Workbook en un array de bytes para la respuesta HTTP
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] excelData = outputStream.toByteArray();

        // Configuración de las cabeceras de la respuesta HTTP
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentDispositionFormData("attachment", "ListaCitas.xlsx"); // Nombre del archivo adjunto
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Tipo de contenido binario

        // Retorno de la respuesta con el archivo Excel en el cuerpo
        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(excelData);
    } catch (IOException e) {
        e.printStackTrace(); // Registro del error en consola
        // Retorno de un error HTTP 500 en caso de excepciones
        return ResponseEntity.status(500).build();
    }
}

}
