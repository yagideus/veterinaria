package com.backend.luaspets.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.backend.luaspets.domain.DTO.SaleRequest;
import com.backend.luaspets.domain.DTO.SaleResponse;
import com.backend.luaspets.persistence.Model.Sale;
import com.backend.luaspets.User.User;

@Mapper(componentModel = "spring", uses = {SaleDetailMapper.class})
public interface SaleMapper {
    
    SaleMapper INSTANCE = Mappers.getMapper(SaleMapper.class);

    @Mapping(target = "idSale", ignore = true)
    @Mapping(target = "saleDate", ignore = true)
    @Mapping(target = "saleStatus", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "userIdToUser")
    @Mapping(target = "saleDetail", source = "saleDetails")
    Sale saleRequestToSale(SaleRequest saleRequest);

    @Mapping(target = "userId", source = "user.id")
    SaleResponse saleToSaleResponse(Sale sale);

    @Named("userIdToUser")
    default User userIdToUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}

