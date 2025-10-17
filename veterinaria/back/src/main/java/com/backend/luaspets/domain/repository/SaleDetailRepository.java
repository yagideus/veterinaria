package com.backend.luaspets.domain.repository;

import java.util.List;

import com.backend.luaspets.domain.DTO.SaleDetailRequest;
import com.backend.luaspets.domain.DTO.SaleDetailResponse;
import com.backend.luaspets.persistance.entity.Product;
import com.backend.luaspets.persistance.entity.Sale;

public interface SaleDetailRepository {

    SaleDetailResponse save(SaleDetailRequest dto, Sale sale, Product product);
    List<SaleDetailResponse> getBySaleId(Integer saleId);
    void delete(Integer idDetail);
}
