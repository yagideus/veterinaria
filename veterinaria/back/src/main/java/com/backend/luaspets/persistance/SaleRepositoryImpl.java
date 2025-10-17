package com.backend.luaspets.persistance;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.backend.luaspets.User.User;
import com.backend.luaspets.domain.DTO.SaleDetailRequest;
import com.backend.luaspets.domain.DTO.SaleRequest;
import com.backend.luaspets.domain.DTO.SaleResponse;
import com.backend.luaspets.domain.repository.SaleDetailRepository;
import com.backend.luaspets.domain.repository.SaleRepository;
import com.backend.luaspets.persistance.crud.AccessoriesRepository;
import com.backend.luaspets.persistance.crud.FoodRepository;
import com.backend.luaspets.persistance.crud.MedicineRepository;
import com.backend.luaspets.persistance.crud.SaleCrudRepository;
import com.backend.luaspets.persistance.entity.Product;
import com.backend.luaspets.persistance.entity.Sale;
import com.backend.luaspets.persistance.mapper.SaleMapper;


@Repository
public class SaleRepositoryImpl implements SaleRepository {

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private SaleDetailRepository saleDetailRepository;

    @Autowired
    private SaleCrudRepository crud;

    @Autowired private FoodRepository foodRepository;
    @Autowired private MedicineRepository medicineRepository;
    @Autowired private AccessoriesRepository accessoriesRepository;

    @Override
    public SaleResponse save(SaleRequest request, User user) {
        
        // Crear entidad Sale desde el mapper
        Sale sale = saleMapper.toEntity(request, user);
        sale.setTotalAmount(BigDecimal.ZERO);
        Sale savedSale = crud.save(sale);
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Procesar cada detalle
        for (SaleDetailRequest dto : request.getSaleDetails()) {
            Product product = resolveProduct(dto.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Producto no encontrado: " + dto.getProductId());
            }

            if (product.getStock() < dto.getQuantity()) {
                throw new IllegalArgumentException("Stock insuficiente para producto: " + dto.getProductId());
            }

            product.setStock(product.getStock() - dto.getQuantity());

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);

            saleDetailRepository.save(dto, savedSale, product);
        }

        savedSale.setTotalAmount(totalAmount);
        Sale updatedSale = crud.save(savedSale);
        return saleMapper.toResponse(updatedSale);
    }

    @Override
    public SaleResponse getById(Integer idSale) {
        Sale sale = crud.findById(idSale)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + idSale));
        return saleMapper.toResponse(sale);
    }

    @Override
    public List<SaleResponse> getAll() {
        return crud.findAll().stream()
            .map(saleMapper::toResponse)
            .collect(Collectors.toList());
    }

    private Product resolveProduct(Integer productId) {
        // Puedes mover esto a un ProductService si quieres desacoplar más
        Product product = foodRepository.findById(productId).orElse(null);
        if (product == null) product = medicineRepository.findById(productId).orElse(null);
        if (product == null) product = accessoriesRepository.findById(productId).orElse(null);
        return product;
    }

    
}


