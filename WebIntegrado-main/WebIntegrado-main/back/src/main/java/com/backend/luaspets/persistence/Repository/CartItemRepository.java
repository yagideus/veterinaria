package com.backend.luaspets.persistence.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Cart;
import com.backend.luaspets.persistence.Model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

     List<CartItem> findByCart(Cart cart);

     void deleteAllByCart(Cart cart);
     
}
