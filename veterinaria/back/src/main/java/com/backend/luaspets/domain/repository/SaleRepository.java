package com.backend.luaspets.domain.repository;

import java.util.List;

import com.backend.luaspets.User.User;
import com.backend.luaspets.domain.DTO.SaleRequest;
import com.backend.luaspets.domain.DTO.SaleResponse;

public interface SaleRepository {
    SaleResponse save(SaleRequest request, User user);
    SaleResponse getById(Integer idSale);
    List<SaleResponse> getAll();
}
