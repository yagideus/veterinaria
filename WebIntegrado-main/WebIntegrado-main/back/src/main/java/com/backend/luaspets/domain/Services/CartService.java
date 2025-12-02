package com.backend.luaspets.domain.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.luaspets.persistence.Model.Cart;
import com.backend.luaspets.persistence.Model.CartItem;
import com.backend.luaspets.persistence.Model.Product;
import com.backend.luaspets.persistence.Repository.AccessoriesRepository;
import com.backend.luaspets.persistence.Repository.CartItemRepository;
import com.backend.luaspets.persistence.Repository.CartRepository;
import com.backend.luaspets.persistence.Repository.FoodRepository;
import com.backend.luaspets.persistence.Repository.MedicineRepository;
import com.backend.luaspets.User.UserRepository;

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

    @Autowired
    private UserRepository userRepository;

    public List<Cart> getAllCarts() {
        return cartRepository.findAll(); // O la lógica para obtener todos los carritos
    }

    // Método para crear un nuevo carrito
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // Método para obtener un carrito por su ID
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Cart getCartById(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        // Forzar la carga de la colección lazy
        cart.getCartItems().size();
        return cart;
    }

    // Método para obtener un carrito por userId
    @org.springframework.transaction.annotation.Transactional
    public Cart getCartByUserId(Integer userId) {
        // Obtener el usuario primero
        com.backend.luaspets.User.User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Obtener el carrito del usuario
        Cart cart = user.getCart();

        if (cart == null) {
            // Si el usuario no tiene carrito, crear uno nuevo
            cart = createCartForUser(user);
        } else {
            // Forzar la carga de la colección lazy
            cart.getCartItems().size();
        }

        return cart;
    }

    // Método auxiliar para crear un carrito para un usuario que no tiene uno
    @org.springframework.transaction.annotation.Transactional
    private Cart createCartForUser(com.backend.luaspets.User.User user) {
        Cart cart = Cart.builder()
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .user(user)
                .cartItems(new java.util.ArrayList<>())
                .build();
        user.setCart(cart);
        cart = cartRepository.save(cart);
        userRepository.save(user);
        return cart;
    }

    // Método para agregar un producto al carrito
    public CartItem addProductToCart(Integer cartId, String productType, Integer productId, Integer quantity) {
        // Verificar si el carrito existe
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Normalizar el tipo de producto a minúsculas
        String normalizedProductType = productType.toLowerCase();

        // Obtener el producto según el tipo y el ID
        Product product = getProductByTypeAndId(normalizedProductType, productId);

        // Verificar si el producto existe
        if (product == null) {
            throw new RuntimeException("Product not found: type=" + normalizedProductType + ", id=" + productId);
        }

        // Verificar si el producto ya está en el carrito
        List<CartItem> existingItems = cartItemRepository.findByCart(cart);
        CartItem existingItem = existingItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId) &&
                               item.getProductType().equalsIgnoreCase(normalizedProductType))
                .findFirst()
                .orElse(null);

        CartItem cartItem;
        if (existingItem != null) {
            // Si el producto ya existe, actualizar la cantidad
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItem = cartItemRepository.save(existingItem);
        } else {
            // Crear nuevo CartItem
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setProductType(normalizedProductType);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice().doubleValue());
            cartItem = cartItemRepository.save(cartItem);
        }

        // Actualizar la fecha de modificación del carrito y refrescar la lista de items
        cart.setUpdatedAt(java.time.LocalDateTime.now());
        // Refrescar el carrito para incluir el nuevo item
        cart = cartRepository.save(cart);
        // Forzar la carga de los items
        cart.getCartItems().size();

        return cartItem;
    }

    // Método para agregar un producto al carrito usando userId
    public CartItem addProductToCartByUserId(Integer userId, String productType, Integer productId, Integer quantity) {
        // Obtener el carrito del usuario
        Cart cart = getCartByUserId(userId);
        // Normalizar el tipo de producto
        String normalizedProductType = productType.toLowerCase();

        // Obtener el producto según el tipo y el ID
        Product product = getProductByTypeAndId(normalizedProductType, productId);

        // Verificar si el producto existe
        if (product == null) {
            throw new RuntimeException("Product not found: type=" + normalizedProductType + ", id=" + productId);
        }

        // Verificar si el producto ya está en el carrito
        List<CartItem> existingItems = cartItemRepository.findByCart(cart);
        CartItem existingItem = existingItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId) &&
                               item.getProductType().equalsIgnoreCase(normalizedProductType))
                .findFirst()
                .orElse(null);

        CartItem cartItem;
        if (existingItem != null) {
            // Si el producto ya existe, actualizar la cantidad
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItem = cartItemRepository.save(existingItem);
        } else {
            // Crear nuevo CartItem
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setProductType(normalizedProductType);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice().doubleValue());
            cartItem = cartItemRepository.save(cartItem);
        }

        // Actualizar la fecha de modificación del carrito
        cart.setUpdatedAt(java.time.LocalDateTime.now());
        cartRepository.save(cart);

        return cartItem;
    }

    // Método para obtener todos los productos de un carrito específico
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CartItem> getAllItemsInCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartItemRepository.findByCart(cart);
    }

    // Método para obtener todos los productos de un carrito por userId
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CartItem> getAllItemsInCartByUserId(Integer userId) {
        Cart cart = getCartByUserId(userId);
        return cartItemRepository.findByCart(cart);
    }

    // Método para actualizar la cantidad de un producto en el carrito
    public CartItem updateProductQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    // Método para eliminar un producto del carrito
    @org.springframework.transaction.annotation.Transactional
    public void removeProductFromCart(Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        Cart cart = cartItem.getCart();

        // Eliminar el item
        cartItemRepository.delete(cartItem);

        // Actualizar la fecha de modificación del carrito
        cart.setUpdatedAt(java.time.LocalDateTime.now());
        cartRepository.save(cart);

        // Forzar la sincronización con la base de datos
        cartItemRepository.flush();
    }

    // Método para limpiar el carrito
    @org.springframework.transaction.annotation.Transactional
    public void clearCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAllByCart(cart);
    }

    // Método auxiliar para obtener un producto según su tipo y ID
    private Product getProductByTypeAndId(String productType, Integer productId) {
        if (productType == null || productId == null) {
            throw new RuntimeException("Product type and ID cannot be null");
        }

        String normalizedType = productType.toLowerCase().trim();
        Product product = null;

        // Intentar buscar en todos los repositorios si el tipo no es claro
        switch (normalizedType) {
            case "food":
                product = foodRepository.findById(productId).orElse(null);
                break;
            case "medicine":
                product = medicineRepository.findById(productId).orElse(null);
                break;
            case "accessory":
            case "accessories":
                product = accessoriesRepository.findById(productId).orElse(null);
                break;
            default:
                // Si el tipo no coincide, intentar buscar en todos los repositorios
                product = foodRepository.findById(productId).orElse(null);
                if (product == null) {
                    product = medicineRepository.findById(productId).orElse(null);
                }
                if (product == null) {
                    product = accessoriesRepository.findById(productId).orElse(null);
                }
                if (product == null) {
                    throw new RuntimeException("Product not found with ID: " + productId + " and type: " + productType);
                }
                break;
        }

        return product;
    }

}
