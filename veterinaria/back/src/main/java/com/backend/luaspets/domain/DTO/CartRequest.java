package com.backend.luaspets.domain.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CartRequest {
    private Integer userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
