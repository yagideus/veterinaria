package com.backend.luaspets.domain.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.domain.DTO.CartRequest;
import com.backend.luaspets.domain.DTO.CartResponse;
import com.backend.luaspets.domain.DTO.CartItemRequest;
import com.backend.luaspets.domain.DTO.CartItemResponse;
import com.backend.luaspets.persistance.crud.AccessoriesRepository;
import com.backend.luaspets.persistance.crud.CartItemRepository;
import com.backend.luaspets.persistance.crud.CartRepository;
import com.backend.luaspets.persistance.crud.FoodRepository;
import com.backend.luaspets.persistance.crud.MedicineRepository;
import com.backend.luaspets.persistance.entity.Cart;
import com.backend.luaspets.persistance.entity.CartItem;
import com.backend.luaspets.persistance.entity.Product;
import com.backend.luaspets.persistance.mapper.CartMapper;
import com.backend.luaspets.persistance.mapper.CartItemMapper;
import com.backend.luaspets.User.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private AccessoriesRepository accessoriesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    // Crear carrito desde DTO
    public CartResponse createCartFromRequest(CartRequest request) {
        Cart cart = new Cart();
        cart.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        cart.setCreatedAt(request.getCreatedAt());
        cart.setUpdatedAt(request.getUpdatedAt());
        Cart saved = cartRepository.save(cart);
        return cartMapper.toCartResponse(saved);
    }

    // Obtener carrito por ID
    public CartResponse getCartById(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartMapper.toCartResponse(cart);
    }

    // Obtener carrito por UserID
    public CartResponse getCartByUserId(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene carrito"));
        return cartMapper.toCartResponse(cart);
    }

    // Obtener todos los carritos
    public List<CartResponse> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(cartMapper::toCartResponse)
                .collect(Collectors.toList());
    }

    // Agregar producto al carrito
    public CartItemResponse addProductToCart(CartItemRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = getProductByTypeAndId(request.getProductType(), request.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setProductType(request.getProductType());
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(product.getPrice().doubleValue());

        CartItem savedItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponse(savedItem);
    }

    // Obtener todos los ítems de un carrito
    public List<CartItemResponse> getAllItemsInCart(Integer cartId) {
        return cartItemRepository.findAll().stream()
                .filter(item -> item.getCart().getIdCart().equals(cartId))
                .map(cartItemMapper::toCartItemResponse)
                .collect(Collectors.toList());
    }

    // Actualizar cantidad de producto
    public CartItemResponse updateProductQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItem.setQuantity(quantity);
        CartItem updated = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponse(updated);
    }

    // Eliminar producto del carrito
    public void removeProductFromCart(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    // Método auxiliar para obtener producto por tipo
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
