package com.backend.luaspets.web.Controller;

import com.backend.luaspets.domain.DTO.ProductDTO;
import com.backend.luaspets.domain.Services.ProductDTOService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductDTOService productService;

    public ProductController(ProductDTOService productService) {
        this.productService = productService;
    }

    // Endpoints generales para todos los productos
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        Optional<ProductDTO> product = productService.getProduct(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable int categoryId) {
        Optional<List<ProductDTO>> products = productService.getByCategory(categoryId);
        return products.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/scarce/{quantity}")
    public ResponseEntity<List<ProductDTO>> getScarceProducts(@PathVariable int quantity) {
        Optional<List<ProductDTO>> products = productService.getScarseProducts(quantity);
        if (products.isPresent() && !products.get().isEmpty()) {
            return ResponseEntity.ok(products.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            // Validar que el tipo de producto esté especificado
            if (productDTO.getProductType() == null || productDTO.getProductType().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            ProductDTO savedProduct = productService.save(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Integer id, 
            @RequestBody ProductDTO productDTO) {
        try {
            productDTO.setId(id);
            ProductDTO updatedProduct = productService.save(productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        if (productService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoints específicos para cada tipo de producto
    @GetMapping("/medicines")
    public ResponseEntity<List<ProductDTO>> getAllMedicines() {
        List<ProductDTO> medicines = productService.getAllMedicines();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/medicines/{id}")
    public ResponseEntity<ProductDTO> getMedicineById(@PathVariable Integer id) {
        Optional<ProductDTO> medicine = productService.getMedicineById(id);
        return medicine.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/medicines")
    public ResponseEntity<ProductDTO> createMedicine(@RequestBody ProductDTO productDTO) {
        try {
            productDTO.setProductType("medicine");
            ProductDTO savedMedicine = productService.save(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMedicine);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/food")
    public ResponseEntity<List<ProductDTO>> getAllFood() {
        List<ProductDTO> food = productService.getAllFood();
        return ResponseEntity.ok(food);
    }

    @GetMapping("/food/{id}")
    public ResponseEntity<ProductDTO> getFoodById(@PathVariable Integer id) {
        Optional<ProductDTO> food = productService.getFoodById(id);
        return food.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/food")
    public ResponseEntity<ProductDTO> createFood(@RequestBody ProductDTO productDTO) {
        try {
            productDTO.setProductType("food");
            ProductDTO savedFood = productService.save(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFood);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/accessories")
    public ResponseEntity<List<ProductDTO>> getAllAccessories() {
        List<ProductDTO> accessories = productService.getAllAccessories();
        return ResponseEntity.ok(accessories);
    }

    @GetMapping("/accessories/{id}")
    public ResponseEntity<ProductDTO> getAccessoryById(@PathVariable Integer id) {
        Optional<ProductDTO> accessory = productService.getAccessoryById(id);
        return accessory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/accessories")
    public ResponseEntity<ProductDTO> createAccessory(@RequestBody ProductDTO productDTO) {
        try {
            productDTO.setProductType("accessory");
            ProductDTO savedAccessory = productService.save(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAccessory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Endpoints de compatibilidad (mantener para no romper funcionalidad existente)
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProductDTO>> getProductsByType(@PathVariable String type) {
        List<ProductDTO> products;
        switch (type.toLowerCase()) {
            case "medicine":
                products = productService.getAllMedicines();
                break;
            case "food":
                products = productService.getAllFood();
                break;
            case "accessory":
                products = productService.getAllAccessories();
                break;
            default:
                products = productService.getAll().stream()
                        .filter(p -> p.getProductType() != null && p.getProductType().equalsIgnoreCase(type))
                        .toList();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<ProductDTO> getProductByTypeAndId(
            @PathVariable String type,
            @PathVariable Integer id) {
        Optional<ProductDTO> product;
        switch (type.toLowerCase()) {
            case "medicine":
                product = productService.getMedicineById(id);
                break;
            case "food":
                product = productService.getFoodById(id);
                break;
            case "accessory":
                product = productService.getAccessoryById(id);
                break;
            default:
                product = productService.getProduct(id)
                        .filter(p -> p.getProductType() != null && p.getProductType().equalsIgnoreCase(type));
        }
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
