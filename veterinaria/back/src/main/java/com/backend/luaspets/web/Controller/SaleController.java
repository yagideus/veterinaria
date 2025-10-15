package com.backend.luaspets.web.Controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.luaspets.User.User;
import com.backend.luaspets.User.UserRepository;
import com.backend.luaspets.domain.DTO.SaleDetailResponse;
import com.backend.luaspets.domain.DTO.SaleRequest;
import com.backend.luaspets.domain.DTO.SaleResponse;
import com.backend.luaspets.domain.Services.SaleService;
import com.backend.luaspets.persistance.entity.Sale;

@RestController
@RequestMapping("/sales")
public class SaleController {
    

    @Autowired
    private SaleService saleService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        List<SaleResponse> responseList = sales.stream().map(sale -> {
            SaleResponse response = new SaleResponse();
            response.setIdSale(sale.getIdSale());
            response.setUserId(sale.getUser().getId()); // Asumiendo que tienes un método getUser()
            response.setSaleDate(sale.getSaleDate());
            response.setTotalAmount(sale.getTotalAmount());
            response.setSaleStatus(sale.getSaleStatus());
            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<List<SaleDetailResponse>> getSaleDetails(@PathVariable Integer saleId) {
        List<SaleDetailResponse> saleDetail = saleService.getSaleDetailsById(saleId);
        return ResponseEntity.ok(saleDetail);
    }

    @PostMapping("/create")
    public ResponseEntity<SaleResponse> createSale(@RequestBody SaleRequest saleDTO) {
    User user = userRepository.findById(saleDTO.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
    Sale sale = saleService.createSale(user, saleDTO.getSaleDetails());

    // Mapear entidad a DTO
    SaleResponse response = new SaleResponse();
    response.setIdSale(sale.getIdSale());
    response.setUserId(user.getId()); // Si necesitas devolver este dato
    response.setSaleDate(sale.getSaleDate());
    response.setTotalAmount(sale.getTotalAmount());
    response.setSaleStatus(sale.getSaleStatus());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

@GetMapping("/export.xlsx")
    public ResponseEntity<byte[]> exportSaleData() {
    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Ventas");

        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Lista de Ventas de Productos");

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
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); // Fusiona desde la columna 0 a la columna 9

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
        String[] headers = {"ID", "ID Usuario", "Fecha Venta", "Total", "Estado"};
        Row headerRow = sheet.createRow(1); // Mover el encabezado a la fila 1, debajo del título
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i); // Ajuste automático de ancho de columna para que el contenido se vea completo
        }

        // Datos
        List<Sale> sales = saleService.getAllSales();
        int rowIndex = 2; // Comienza en la fila 2, después del título y el encabezado
        for (Sale sale : sales) {
            Row row = sheet.createRow(rowIndex++);

            Cell cellId = row.createCell(0);
            cellId.setCellValue(sale.getIdSale());
            cellId.setCellStyle(dataStyle);

            Cell cellName = row.createCell(1);
            cellName.setCellValue(sale.getUser().getId());
            cellName.setCellStyle(dataStyle);

            Cell cellBrand = row.createCell(2);
            cellBrand.setCellValue(sale.getSaleDate());
            cellBrand.setCellStyle(dataStyle);

            Cell cellDescription = row.createCell(3);
            cellDescription.setCellValue(sale.getTotalAmount().doubleValue());
            cellDescription.setCellStyle(dataStyle);

            Cell cellPrice = row.createCell(4);
            cellPrice.setCellValue(sale.getSaleStatus());
            cellPrice.setCellStyle(currencyStyle);

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
