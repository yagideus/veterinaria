package com.backend.luaspets.domain.DTO;

import lombok.Data;

@Data
public class SaleDetailRequest {
    
    private Integer productId;
    private Integer quantity;
}
