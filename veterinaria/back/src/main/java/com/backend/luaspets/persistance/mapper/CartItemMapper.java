package com.backend.luaspets.persistance.mapper;

import com.backend.luaspets.domain.DTO.CartItemResponse;
import com.backend.luaspets.persistance.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "idCartItem", target = "cartItemId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "price", target = "unitPrice")
    @Mapping(target = "totalPrice", expression = "java(cartItem.getPrice() * cartItem.getQuantity())")
    CartItemResponse toCartItemResponse(CartItem cartItem);
}
