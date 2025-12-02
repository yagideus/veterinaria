package Util;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet; // Corregido: ss en lugar de sl
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("/api/v1/accessories.xlsx")
public class ListFood extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"listado-comida.xlsx\"");
        Sheet hoja = workbook.createSheet("Comidas"); 
    }
}
