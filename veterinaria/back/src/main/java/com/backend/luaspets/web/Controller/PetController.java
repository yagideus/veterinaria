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

import com.backend.luaspets.domain.DTO.PetRequest;
import com.backend.luaspets.domain.DTO.PetResponse;
import com.backend.luaspets.domain.Services.PetService;
import com.backend.luaspets.persistance.entity.Pet;

@RestController
@RequestMapping("/pets")
public class PetController {
    
    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<PetResponse>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    // Obtener una mascota por ID
     @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPetById(@PathVariable Integer id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<PetResponse> createPet(@RequestBody PetRequest request) {
        PetResponse response = petService.createPet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // Obtener todas las mascotas de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PetResponse>> getPetsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(petService.getPetsByUserId(userId));
    }

    // Actualizar los campos de una mascota por su ID
    @PutMapping("/{petId}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Integer petId, @RequestBody PetRequest request) {
        return ResponseEntity.ok(petService.updatePet(petId, request));
    }

    // Eliminar una mascota por su ID
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePetById(@PathVariable Integer petId) {
        petService.deletePetById(petId);
        return ResponseEntity.noContent().build();
    }

     /* excel */
    @GetMapping("/export.xlsx")
    public ResponseEntity<byte[]> exportFoodData() {
    try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Mascotas");

            // Título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Lista de Mascotas");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // Encabezado
            String[] headers = {"ID", "Nombre", "Especie", "Raza", "Tamaño", "Peso", "Edad", "Género", "ID Dueño"};
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);

            Row headerRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // Estilos de datos
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            List<PetResponse> pets = petService.getAllPets();
            int rowIndex = 2;
            for (PetResponse pet : pets) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(pet.getId());
                row.createCell(1).setCellValue(pet.getName());
                row.createCell(2).setCellValue(pet.getSpecies());
                row.createCell(3).setCellValue(pet.getBreed());
                row.createCell(4).setCellValue(pet.getSize());
                row.createCell(5).setCellValue(pet.getWeight());
                row.createCell(6).setCellValue(pet.getAge());
                row.createCell(7).setCellValue(pet.getGender());
                row.createCell(8).setCellValue(pet.getUserId());

                for (int i = 0; i <= 8; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelData = outputStream.toByteArray();

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.setContentDispositionFormData("attachment", "Mascotas.xlsx");
            headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headersResponse)
                    .body(excelData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
