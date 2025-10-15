package com.backend.luaspets.persistance.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Cart;
import com.backend.luaspets.persistance.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

     List<CartItem> findByCart(Cart cart);
     
}
