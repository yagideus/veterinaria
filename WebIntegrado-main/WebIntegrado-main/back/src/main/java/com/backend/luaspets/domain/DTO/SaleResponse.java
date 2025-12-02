package com.backend.luaspets.domain.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SaleResponse {
    private Integer idSale;
    private Integer userId; // O el dato necesario del usuario
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private String saleStatus;
}
