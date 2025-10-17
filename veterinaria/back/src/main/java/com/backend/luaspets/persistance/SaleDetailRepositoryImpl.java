package com.backend.luaspets.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.backend.luaspets.domain.DTO.SaleDetailRequest;
import com.backend.luaspets.domain.DTO.SaleDetailResponse;
import com.backend.luaspets.domain.repository.SaleDetailRepository;
import com.backend.luaspets.persistance.crud.ProductCrudRepository;
import com.backend.luaspets.persistance.crud.SaleDetailCrudRepository;
import com.backend.luaspets.persistance.crud.SaleCrudRepository;
import com.backend.luaspets.persistance.entity.Product;
import com.backend.luaspets.persistance.entity.Sale;
import com.backend.luaspets.persistance.entity.SaleDetail;
import com.backend.luaspets.persistance.mapper.SaleDetailMapper;


@Repository
public class SaleDetailRepositoryImpl implements SaleDetailRepository {

    @Autowired
    private SaleDetailCrudRepository crud;

    @Autowired
    private SaleCrudRepository saleRepository;

    @Autowired
    private ProductCrudRepository productRepository;

    @Autowired
    private SaleDetailMapper mapper;

    @Override
    public SaleDetailResponse save(SaleDetailRequest dto, Sale sale, Product product) {
        SaleDetail entity = mapper.toEntity(dto, sale, product);
        SaleDetail saved = crud.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public List<SaleDetailResponse> getBySaleId(Integer saleId) {
        Sale sale = saleRepository.findById(saleId)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + saleId));
        List<SaleDetail> details = crud.findBySale(sale);
        return details.stream()
                      .map(mapper::toResponse)
                      .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer idDetail) {
        crud.deleteById(idDetail);
    }
}


