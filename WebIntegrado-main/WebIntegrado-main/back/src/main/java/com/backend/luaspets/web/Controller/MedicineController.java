package com.backend.luaspets.web.Controller;

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

import com.backend.luaspets.persistence.Model.Medicine;
import com.backend.luaspets.domain.Services.MedicineService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/medicine")
@CrossOrigin(origins = { "http://localhost:4200" })
public class MedicineController {
    private final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    // Endpoint para obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Medicine>> getAllMedicine() {
        List<Medicine> medicines = medicineService.getAllMedicine();
        return ResponseEntity.ok(medicines);
    }

    // Endpoint para obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Integer id) {
        Optional<Medicine> medicine = medicineService.getMedicineById(id);
        return medicine.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para agregar un nuevo producto
    @PostMapping
    public ResponseEntity<Medicine> createMedicine(@RequestBody Medicine medicine) {
        try {
            Medicine newMedicine = medicineService.saveMedicine(medicine);
            return ResponseEntity.status(HttpStatus.CREATED).body(newMedicine);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Producto ya existe
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Endpoint para actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Integer id, @RequestBody Medicine medicineDetails) {
        try {
            Medicine updatedMedicine = medicineService.updateMedicine(id, medicineDetails);
            return ResponseEntity.ok(updatedMedicine);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint para eliminar un producto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Integer id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/export.xlsx")
    public ResponseEntity<byte[]> exportFoodData() {
    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Medicamentos");

        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Lista del Almacén de Medicamentos Para Mascotas");

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
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9)); // Fusiona desde la columna 0 a la columna 9

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
        String[] headers = {"ID", "Nombre", "Marca", "Descripción", "Precio", "Stock", "Categoría", "Imagen URL", "Fecha de Registro", "Fecha de Expiración"};
        Row headerRow = sheet.createRow(1); // Mover el encabezado a la fila 1, debajo del título
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i); // Ajuste automático de ancho de columna para que el contenido se vea completo
        }

        // Datos
        List<Medicine> medicines = medicineService.getAllMedicine();
        int rowIndex = 2; // Comienza en la fila 2, después del título y el encabezado
        for (Medicine medicine : medicines) {
            Row row = sheet.createRow(rowIndex++);

            Cell cellId = row.createCell(0);
            cellId.setCellValue(medicine.getId());
            cellId.setCellStyle(dataStyle);

            Cell cellName = row.createCell(1);
            cellName.setCellValue(medicine.getName());
            cellName.setCellStyle(dataStyle);

            Cell cellBrand = row.createCell(2);
            cellBrand.setCellValue(medicine.getBrand() != null ? medicine.getBrand() : "");
            cellBrand.setCellStyle(dataStyle);

            Cell cellDescription = row.createCell(3);
            cellDescription.setCellValue(medicine.getDescription() != null ? medicine.getDescription() : "");
            cellDescription.setCellStyle(dataStyle);

            Cell cellPrice = row.createCell(4);
            cellPrice.setCellValue(medicine.getPrice() != null ? medicine.getPrice().doubleValue() : 0.0);
            cellPrice.setCellStyle(currencyStyle);

            Cell cellStock = row.createCell(5);
            cellStock.setCellValue(medicine.getStock() != null ? medicine.getStock() : 0);
            cellStock.setCellStyle(dataStyle);

            Cell cellCategory = row.createCell(6);
            cellCategory.setCellValue(medicine.getCategory() != null ? medicine.getCategory() : "");
            cellCategory.setCellStyle(dataStyle);

            Cell cellImageUrl = row.createCell(7);
            cellImageUrl.setCellValue(medicine.getImage_url() != null ? medicine.getImage_url() : "");
            cellImageUrl.setCellStyle(dataStyle);

            Cell cellCreatedAt = row.createCell(8);
            cellCreatedAt.setCellValue(medicine.getCreated_at() != null ? medicine.getCreated_at().toString() : "");
            cellCreatedAt.setCellStyle(dataStyle);

            Cell cellExpirationDate = row.createCell(9);
            cellExpirationDate.setCellValue(medicine.getExpiration_date() != null ? medicine.getExpiration_date().toString() : "");
            cellExpirationDate.setCellStyle(dataStyle);
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
