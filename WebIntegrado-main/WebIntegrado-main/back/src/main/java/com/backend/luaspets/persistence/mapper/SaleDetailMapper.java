package com.backend.luaspets.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.backend.luaspets.domain.DTO.SaleDetailRequest;
import com.backend.luaspets.domain.DTO.SaleDetailResponse;
import com.backend.luaspets.persistence.Model.Product;
import com.backend.luaspets.persistence.Model.SaleDetail;

@Mapper(componentModel = "spring")
public interface SaleDetailMapper {
    
    SaleDetailMapper INSTANCE = Mappers.getMapper(SaleDetailMapper.class);

    @Mapping(target = "idDetail", ignore = true)
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "product", source = "productId", qualifiedByName = "productIdToProduct")
    SaleDetail saleDetailRequestToSaleDetail(SaleDetailRequest saleDetailRequest);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "userFullName", source = "sale.user.fullName")
    SaleDetailResponse saleDetailToSaleDetailResponse(SaleDetail saleDetail);

    @Named("productIdToProduct")
    default Product productIdToProduct(Integer productId) {
        // Este método debe ser manejado en el servicio ya que Product es abstracta
        // El servicio deberá obtener el producto del repositorio
        return null;
    }
}

