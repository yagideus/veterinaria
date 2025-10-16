package com.backend.luaspets.domain.DTO;

import lombok.Data;

@Data
public class CartItemResponse {
    private Integer cartItemId;
    private String productType;
    private Integer productId;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;
}
