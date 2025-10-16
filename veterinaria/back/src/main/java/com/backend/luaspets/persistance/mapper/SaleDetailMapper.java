package com.backend.luaspets.persistance.mapper;

import org.springframework.stereotype.Component;

import com.backend.luaspets.domain.DTO.SaleDetailRequest;
import com.backend.luaspets.domain.DTO.SaleDetailResponse;
import com.backend.luaspets.persistance.entity.Product;
import com.backend.luaspets.persistance.entity.Sale;
import com.backend.luaspets.persistance.entity.SaleDetail;

@Component
public class SaleDetailMapper {

    public SaleDetailResponse toResponse(SaleDetail entity) {
        SaleDetailResponse dto = new SaleDetailResponse();
        dto.setIdDetail(entity.getIdDetail());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setUserFullName(entity.getSale().getUser().getFullName());
        return dto;
    }

    public SaleDetail toEntity(SaleDetailRequest dto, Sale sale, Product product) {
        SaleDetail entity = new SaleDetail();
        entity.setSale(sale);
        entity.setProduct(product);
        entity.setQuantity(dto.getQuantity());
        entity.setUnitPrice(product.getPrice()); // lógica de negocio
        return entity;
    }
}
