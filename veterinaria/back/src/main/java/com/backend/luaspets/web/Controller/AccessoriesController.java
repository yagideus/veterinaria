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

import com.backend.luaspets.domain.Services.AccessoriesService;
import com.backend.luaspets.persistance.entity.Accessories;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accessories")
public class AccessoriesController {

    private final AccessoriesService accessoriesService;

    @Autowired
    public AccessoriesController(AccessoriesService accessoriesService) {
        this.accessoriesService = accessoriesService;
    }

    // Endpoint para obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Accessories>> getAllFood() {
        List<Accessories> accessories = accessoriesService.getAllAccessories();
        return ResponseEntity.ok(accessories);
    }

    // Endpoint para obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Accessories> getAccessoriesById(@PathVariable Integer id) {
        Optional<Accessories> accessories = accessoriesService.getAccessoriesById(id);
        return accessories.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para agregar un nuevo producto
    @PostMapping
    public ResponseEntity<Accessories> createAccessories(@RequestBody Accessories accessories) {
        try {
            Accessories newAccessories = accessoriesService.saveAccessories(accessories);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAccessories);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Producto ya existe
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Endpoint para actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Accessories> updateAccessories(@PathVariable Integer id,
            @RequestBody Accessories accessoriesDetails) {
        try {
            Accessories updatedAccessories = accessoriesService.updateAccessories(id, accessoriesDetails);
            return ResponseEntity.ok(updatedAccessories);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint para eliminar un producto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessories(@PathVariable Integer id) {
        try {
            accessoriesService.deleteAccessories(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/export.xlsx")
public ResponseEntity<byte[]> exportMedicineData() {
    try (Workbook workbook = new XSSFWorkbook()) {
        // Crear una hoja de trabajo con nombre
        Sheet sheet = workbook.createSheet("Accesorios");

        // Título del archivo
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Lista del Almacén de Accesorios Para Mascotas");

        // Estilo para el título
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Aplicar estilo al título y fusionar celdas para centrarlo
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9)); // Fusiona celdas de la columna 0 a la 9

        // Estilo para los encabezados de las columnas
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

        // Estilo para celdas de datos
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);

        // Estilo para celdas de tipo moneda (precio)
        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.cloneStyleFrom(dataStyle);
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));

        // Encabezados de las columnas
        String[] headers = {
            "ID", "Nombre", "Marca", "Descripción", "Precio", 
            "Stock", "Categoría", "Imagen URL", "Fecha de Registro", "Fecha de Expiración"
        };
        Row headerRow = sheet.createRow(1); // Encabezado en la segunda fila
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i); // Ajusta automáticamente el ancho de la columna
        }

        // Obtener los datos de la base de datos
        List<Accessories> medicines = accessoriesService.getAllAccessories();
        int rowIndex = 2; // Comienza después de la fila del título y el encabezado

        // Llenar la hoja con los datos de los accesorios
        for (Accessories accessories : medicines) {
            Row row = sheet.createRow(rowIndex++);

            // ID
            Cell cellId = row.createCell(0);
            cellId.setCellValue(accessories.getId());
            cellId.setCellStyle(dataStyle);

            // Nombre
            Cell cellName = row.createCell(1);
            cellName.setCellValue(accessories.getName());
            cellName.setCellStyle(dataStyle);

            // Marca
            Cell cellBrand = row.createCell(2);
            cellBrand.setCellValue(accessories.getBrand() != null ? accessories.getBrand() : "");
            cellBrand.setCellStyle(dataStyle);

            // Descripción
            Cell cellDescription = row.createCell(3);
            cellDescription.setCellValue(accessories.getDescription() != null ? accessories.getDescription() : "");
            cellDescription.setCellStyle(dataStyle);

            // Precio
            Cell cellPrice = row.createCell(4);
            cellPrice.setCellValue(accessories.getPrice() != null ? accessories.getPrice().doubleValue() : 0.0);
            cellPrice.setCellStyle(currencyStyle);

            // Stock
            Cell cellStock = row.createCell(5);
            cellStock.setCellValue(accessories.getStock() != null ? accessories.getStock() : 0);
            cellStock.setCellStyle(dataStyle);

            // Categoría
            Cell cellCategory = row.createCell(6);
            cellCategory.setCellValue(accessories.getCategory() != null ? accessories.getCategory() : "");
            cellCategory.setCellStyle(dataStyle);

            // URL de la imagen
            Cell cellImageUrl = row.createCell(7);
            cellImageUrl.setCellValue(accessories.getImage_url() != null ? accessories.getImage_url() : "");
            cellImageUrl.setCellStyle(dataStyle);

            // Fecha de registro
            Cell cellCreatedAt = row.createCell(8);
            cellCreatedAt.setCellValue(accessories.getCreated_at() != null ? accessories.getCreated_at().toString() : "");
            cellCreatedAt.setCellStyle(dataStyle);

            // Fecha de expiración
            Cell cellExpirationDate = row.createCell(9);
            cellExpirationDate.setCellValue(accessories.getExpiration_date() != null ? accessories.getExpiration_date().toString() : "");
            cellExpirationDate.setCellStyle(dataStyle);
        }

        // Ajustar el ancho de las columnas manualmente para evitar que el contenido quede cortado
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000); // Añadir espacio extra para mejor visibilidad
        }

        // Convertir el workbook a un array de bytes para enviarlo como respuesta
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] excelData = outputStream.toByteArray();

        // Configurar los encabezados de la respuesta HTTP para descargar el archivo
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentDispositionFormData("attachment", "AlmacenAccesorios.xlsx");
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Devolver la respuesta con el archivo en el cuerpo de la respuesta
        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(excelData);
    } catch (IOException e) {
        e.printStackTrace(); // Imprimir la traza de la excepción para facilitar la depuración
        return ResponseEntity.status(500).build(); // Devolver error 500 si ocurre una excepción
    }
}
}
