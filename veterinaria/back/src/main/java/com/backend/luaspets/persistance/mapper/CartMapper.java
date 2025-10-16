package com.backend.luaspets.persistance.mapper;

import com.backend.luaspets.domain.DTO.CartResponse;
import com.backend.luaspets.persistance.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {

    @Mapping(source = "idCart", target = "idCart")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "cartItems", target = "items")
    CartResponse toCartResponse(Cart cart);
}
