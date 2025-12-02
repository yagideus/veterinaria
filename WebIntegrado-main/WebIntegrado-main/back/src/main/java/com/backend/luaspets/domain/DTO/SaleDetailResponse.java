package com.backend.luaspets.domain.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SaleDetailResponse {
    private Integer idDetail;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer productId;
    private String productName;
    private String userFullName;
}
