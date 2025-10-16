package com.backend.luaspets.domain.DTO;

import lombok.Data;

@Data
public class CartItemRequest {
    private Integer cartId;
    private String productType; // "food", "medicine", "accessory"
    private Integer productId;
    private Integer quantity;
}
