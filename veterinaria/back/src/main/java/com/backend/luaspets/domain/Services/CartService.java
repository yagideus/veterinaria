package com.backend.luaspets.domain.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.persistance.crud.AccessoriesRepository;
import com.backend.luaspets.persistance.crud.CartItemRepository;
import com.backend.luaspets.persistance.crud.CartRepository;
import com.backend.luaspets.persistance.crud.FoodRepository;
import com.backend.luaspets.persistance.crud.MedicineRepository;
import com.backend.luaspets.persistance.entity.Cart;
import com.backend.luaspets.persistance.entity.CartItem;
import com.backend.luaspets.persistance.entity.Product;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private AccessoriesRepository accessoriesRepository;

    public List<Cart> getAllCarts() {
        return cartRepository.findAll(); // O la lógica para obtener todos los carritos
    }

    // Método para crear un nuevo carrito
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // Método para obtener un carrito por su ID
    public Cart getCartById(Integer cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    // Método para agregar un producto al carrito
    public CartItem addProductToCart(Integer cartId, String productType, Integer productId, Integer quantity) {
        // Verificar si el carrito existe
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Obtener el producto según el tipo y el ID
        Product product = getProductByTypeAndId(productType, productId);

        // Verificar si el producto existe
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Crear y guardar el CartItem
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product); // Establecer relación con el producto
        cartItem.setProductType(productType);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getPrice().doubleValue());

        return cartItemRepository.save(cartItem);
    }

    // Método para obtener todos los productos de un carrito específico
    public List<CartItem> getAllItemsInCart(Integer cartId) {
        return cartItemRepository.findAll().stream()
                .filter(item -> item.getCart().getIdCart().equals(cartId))
                .toList();
    }

    // Método para actualizar la cantidad de un producto en el carrito
    public CartItem updateProductQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
    
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    // Método para eliminar un producto del carrito
    public void removeProductFromCart(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    // Método auxiliar para obtener un producto según su tipo y ID
    private Product getProductByTypeAndId(String productType, Integer productId) {
        switch (productType.toLowerCase()) {
            case "food":
                return foodRepository.findById(productId).orElse(null);
            case "medicine":
                return medicineRepository.findById(productId).orElse(null);
            case "accessory":
                return accessoriesRepository.findById(productId).orElse(null);
            default:
                throw new RuntimeException("Invalid product type");
        }
    }

}
