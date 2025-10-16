package com.backend.luaspets.domain.DTO;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CartResponse {
    private Integer idCart;
    private Integer userId;
    private String userFullName; // opcional, si quieres mostrarlo
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemResponse> items;
}