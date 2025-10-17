package com.backend.luaspets.persistance.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.backend.luaspets.User.User;
import com.backend.luaspets.domain.DTO.SaleRequest;
import com.backend.luaspets.domain.DTO.SaleResponse;
import com.backend.luaspets.persistance.entity.Sale;

@Component
public class SaleMapper {

    public SaleResponse toResponse(Sale sale) {
        SaleResponse dto = new SaleResponse();
        dto.setIdSale(sale.getIdSale());
        dto.setUserId(sale.getUser().getId());
        dto.setSaleDate(sale.getSaleDate());
        dto.setTotalAmount(sale.getTotalAmount());
        dto.setSaleStatus(sale.getSaleStatus());
        return dto;
    }

    public Sale toEntity(SaleRequest request, User user) {
        Sale sale = new Sale();
        sale.setUser(user);
        sale.setSaleDate(LocalDateTime.now());
        sale.setSaleStatus("PENDIENTE");
        sale.setTotalAmount(BigDecimal.ZERO);
        return sale;
    }
}
