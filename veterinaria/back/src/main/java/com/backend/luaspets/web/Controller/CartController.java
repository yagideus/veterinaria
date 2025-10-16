package com.backend.luaspets.web.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.luaspets.domain.DTO.CartRequest;
import com.backend.luaspets.domain.DTO.CartResponse;
import com.backend.luaspets.domain.DTO.CartItemRequest;
import com.backend.luaspets.domain.DTO.CartItemResponse;
import com.backend.luaspets.domain.Services.CartService;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    // Obtener todos los carritos (DTO)
    @GetMapping
    public ResponseEntity<List<CartResponse>> getAllCarts() {
        List<CartResponse> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    // Crear un nuevo carrito
    @PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestBody CartRequest request) {
        CartResponse createdCart = cartService.createCartFromRequest(request);
        return ResponseEntity.ok(createdCart);
    }

    // Obtener un carrito por ID
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Integer cartId) {
        CartResponse cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(cart);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Integer userId) {
        CartResponse cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }


    // Agregar un producto al carrito
    @PostMapping("/item")
    public ResponseEntity<CartItemResponse> addProductToCart(@RequestBody CartItemRequest request) {
        try {
            if (request.getCartId() == null || request.getProductId() == null ||
                    request.getProductType() == null || request.getQuantity() == null) {
                return ResponseEntity.badRequest().build();
            }

            CartItemResponse addedItem = cartService.addProductToCart(request);
            return ResponseEntity.ok(addedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Obtener todos los productos de un carrito
    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemResponse>> getAllItemsInCart(@PathVariable Integer cartId) {
        List<CartItemResponse> items = cartService.getAllItemsInCart(cartId);
        return ResponseEntity.ok(items);
    }

    // Actualizar la cantidad de un producto en el carrito
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateProductQuantity(
            @PathVariable Integer cartItemId,
            @RequestBody Map<String, Integer> request) {
        Integer quantity = request.get("quantity");

        if (quantity == null || quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }

        CartItemResponse updatedItem = cartService.updateProductQuantity(cartItemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    // Eliminar un producto del carrito
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Integer cartItemId) {
        cartService.removeProductFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }

}
