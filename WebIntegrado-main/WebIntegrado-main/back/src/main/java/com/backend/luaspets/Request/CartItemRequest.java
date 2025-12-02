package com.backend.luaspets.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartItemRequest {

    private Integer productId;
    private String productType;
    private Integer quantity;
    
}
