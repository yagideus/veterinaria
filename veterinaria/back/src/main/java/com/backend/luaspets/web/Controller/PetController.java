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
        List<Pet> pets = petService.getAllPets();
        List<PetResponse> responses = pets.stream().map(pet -> {
            PetResponse response = new PetResponse();
            
            response.setId(pet.getId());
            response.setUserId(pet.getOwner().getId());
            response.setUserName(pet.getOwner().getFullName());
            response.setName(pet.getName());
            response.setSpecies(pet.getSpecies());
            response.setBreed(pet.getBreed());
            response.setSize(pet.getSize());
            response.setWeight(pet.getWeight());
            response.setAge(pet.getAge());
            response.setGender(pet.getGender());
            return response;
        }).toList();

        return ResponseEntity.ok(responses);
    }

    // Obtener una mascota por ID
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPetById(@PathVariable Integer id) {
        PetResponse response = petService.getPetById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<PetResponse> createPet(@RequestBody PetRequest request){

        PetResponse response = new PetResponse();
        Pet pet = petService.createPet(request);
        
        response.setId(pet.getId());
        response.setUserId(request.getUserId());
        response.setUserName(pet.getOwner().getFullName());
        response.setName(request.getName());
        response.setSpecies(request.getSpecies());
        response.setBreed(request.getBreed());
        response.setSize(request.getSize());
        response.setWeight(request.getWeight());
        response.setAge(request.getAge());
        response.setGender(request.getGender());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Obtener todas las mascotas de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PetResponse>> getPetsByUserId(@PathVariable Integer userId) {
        List<Pet> pets = petService.getPetsByUserId(userId);
        List<PetResponse> responses = pets.stream().map(pet -> {
            PetResponse response = new PetResponse();
            response.setId(pet.getId());
            response.setUserName(pet.getOwner().getFullName());
            response.setName(pet.getName());
            response.setSpecies(pet.getSpecies());
            response.setBreed(pet.getBreed());
            response.setSize(pet.getSize());
            response.setWeight(pet.getWeight());
            response.setAge(pet.getAge());
            response.setGender(pet.getGender());
            return response;
        }).toList();

        return ResponseEntity.ok(responses);
    }

    // Actualizar los campos de una mascota por su ID
    @PutMapping("/{petId}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Integer petId, @RequestBody PetRequest request) {
        Pet updatedPet = petService.updatePet(petId, request);

        PetResponse response = new PetResponse();
        response.setId(updatedPet.getId());
        response.setUserId(updatedPet.getOwner().getId());
        response.setUserName(updatedPet.getOwner().getFullName());
        response.setName(updatedPet.getName());
        response.setSpecies(updatedPet.getSpecies());
        response.setBreed(updatedPet.getBreed());
        response.setSize(updatedPet.getSize());
        response.setWeight(updatedPet.getWeight());
        response.setAge(updatedPet.getAge());
        response.setGender(updatedPet.getGender());

        return ResponseEntity.ok(response);
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

        // Estilo del título
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Aplicar el estilo al título y fusionar celdas
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8)); // Fusiona desde la columna 0 a la columna 9

        // Estilo para el encabezado
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

        // Estilo para las celdas de datos
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);

        // Estilo para celdas de precio (número con formato de moneda)
        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.cloneStyleFrom(dataStyle);
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));

        // Encabezados
        String[] headers = {"ID", "Nombre", "Especie", "Raza", "Tamaño", "Peso", "Edad", "Género", "ID Dueño"};
        Row headerRow = sheet.createRow(1); // Mover el encabezado a la fila 1, debajo del título
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i); // Ajuste automático de ancho de columna para que el contenido se vea completo
        }

        // Datos
        List<Pet> pets = petService.getAllPets();
        int rowIndex = 2; // Comienza en la fila 2, después del título y el encabezado
        for (Pet pet : pets) {
            Row row = sheet.createRow(rowIndex++);

            Cell cellId = row.createCell(0);
            cellId.setCellValue(pet.getId());
            cellId.setCellStyle(dataStyle);

            Cell cellName = row.createCell(1);
            cellName.setCellValue(pet.getName());
            cellName.setCellStyle(dataStyle);

            Cell cellBrand = row.createCell(2);
            cellBrand.setCellValue(pet.getSpecies());
            cellBrand.setCellStyle(dataStyle);

            Cell cellDescription = row.createCell(3);
            cellDescription.setCellValue(pet.getBreed());
            cellDescription.setCellStyle(dataStyle);

            Cell cellPrice = row.createCell(4);
            cellPrice.setCellValue(pet.getSize());
            cellPrice.setCellStyle(currencyStyle);

            Cell cellStock = row.createCell(5);
            cellStock.setCellValue(pet.getWeight());
            cellStock.setCellStyle(dataStyle);

            Cell cellCategory = row.createCell(6);
            cellCategory.setCellValue(pet.getAge());
            cellCategory.setCellStyle(dataStyle);

            Cell cellImageUrl = row.createCell(7);
            cellImageUrl.setCellValue(pet.getGender());
            cellImageUrl.setCellStyle(dataStyle);

            Cell cellCreatedAt = row.createCell(8);
            cellCreatedAt.setCellValue(pet.getOwner().getId());
            cellCreatedAt.setCellStyle(dataStyle);

        }

        // Ajustar ancho de columnas manualmente en caso de que autoSizeColumn no sea suficiente
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000); // Añadir espacio adicional para evitar que el contenido quede cortado
        }

        // Convertir el Workbook en un byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] excelData = outputStream.toByteArray();

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentDispositionFormData("attachment", "AlmacenComidas.xlsx");
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
