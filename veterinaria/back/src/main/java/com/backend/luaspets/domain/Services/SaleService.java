package com.backend.luaspets.domain.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.User.User;
import com.backend.luaspets.domain.DTO.SaleDetailRequest;
import com.backend.luaspets.domain.DTO.SaleDetailResponse;
import com.backend.luaspets.domain.DTO.SaleRequest;
import com.backend.luaspets.domain.DTO.SaleResponse;
import com.backend.luaspets.domain.repository.SaleDetailRepository;
import com.backend.luaspets.domain.repository.SaleRepository;

import jakarta.transaction.Transactional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleDetailRepository saleDetailRepository;


    public List<SaleResponse> getAllSales() {
        return saleRepository.getAll();
    }

    public List<SaleDetailResponse> getSaleDetailsById(Integer saleId) {
        return saleDetailRepository.getBySaleId(saleId);
    }

    @Transactional
    public SaleResponse createSale(User user, List<SaleDetailRequest> saleDetailsDTO) {
        
        SaleRequest request = new SaleRequest();
        request.setUserId(user.getId());
        request.setSaleDetails(saleDetailsDTO);
        return saleRepository.save(request, user);
        // // Crear la venta
        // Sale sale = new Sale();
        // sale.setUser(user);
        // sale.setSaleDate(LocalDateTime.now());
        // sale.setSaleStatus("COMPLETADO");
        // BigDecimal totalAmount = BigDecimal.ZERO;

        // for (SaleDetailRequest dto : saleDetailsDTO) {
        //     Product product = resolveProduct(dto.getProductId());
        //     if (product == null) {
        //         throw new IllegalArgumentException("Product with ID " + dto.getProductId() + " not found.");
        //     }

        //     if (product.getStock() < dto.getQuantity()) {
        //         throw new IllegalArgumentException("Not enough stock for product with ID " + dto.getProductId());
        //     }

        //     product.setStock(product.getStock() - dto.getQuantity());

        //     // Calcular subtotal y acumular
        //     BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));
        //     totalAmount = totalAmount.add(lineTotal);

        //     // Guardar detalle usando el repositorio desacoplado
        //     saleDetailRepository.save(dto, sale, product);
        // }

        // sale.setTotalAmount(totalAmount);
        // saleRepository.save(sale);

        // return sale;
    }

}
