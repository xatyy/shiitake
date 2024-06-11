package ro.foodx.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.dto.category.CategoryCreateRequest;
import ro.foodx.backend.dto.category.CategoryCreateResponse;
import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.dto.product.ProductEditResponse;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateResponse;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.dto.store.StoreEditResponse;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.security.utils.SecurityConstants;
import ro.foodx.backend.service.CategoryService;
import ro.foodx.backend.service.ProductService;
import ro.foodx.backend.service.StoreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public StoreController(StoreService storeService, ProductService productService, CategoryService categoryService) {
        this.storeService = storeService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<Store>> getAllStores() {
        List<Store> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }
    /*
    @GetMapping("/{storeId}/product")
    public ResponseEntity<List<Product>> getProductsByStoreId(@PathVariable Long storeId, int pageSize, int pageNumber) {
        Page<Product> products = productService.getProductsByStoreId(storeId, pageNumber, pageSize);
        return ResponseEntity.ok(products);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        Optional<Store> store = storeService.getStoreById(id);

        return store.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StoreCreateResponse> addStore(@Valid @RequestBody StoreCreateRequest storeCreateRequest, HttpServletRequest request ) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final StoreCreateResponse storeCreateResponse = storeService.saveStore(storeCreateRequest, jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeCreateResponse);
    }

    @PostMapping("/{storeId}/product")
    public ResponseEntity<ProductCreateResponse> addProduct(@Valid @PathVariable Long storeId, @RequestBody ProductCreateRequest productCreateRequest, HttpServletRequest request ) {

        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final ProductCreateResponse productCreateResponse = productService.saveProduct(productCreateRequest, storeId, jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(productCreateResponse);
    }

    @PostMapping("/{storeId}/category")
    public ResponseEntity<CategoryCreateResponse> addCategory(@Valid @PathVariable Long storeId, @RequestBody CategoryCreateRequest categoryCreateRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token ) {

        final CategoryCreateResponse categoryCreateResponse = categoryService.saveCategory(categoryCreateRequest, storeId, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryCreateResponse);
    }

    @PatchMapping("/{storeId}/product/{id}")
    public ResponseEntity<ProductEditResponse> updateProduct(@Valid @PathVariable Long storeId, @PathVariable Long id, @RequestBody ProductEditRequest productEditRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        final ProductEditResponse productEditResponse = productService.editProduct(productEditRequest, storeId, id, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(productEditResponse);
    }



    @PatchMapping("/{id}")
    public ResponseEntity<StoreEditResponse> updateStore(@Valid @PathVariable Long id, @RequestBody StoreEditRequest storeEditRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        final StoreEditResponse storeEditResponse = storeService.editStore(storeEditRequest, id, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeEditResponse);
    }


     @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
         storeService.deleteStore(id);
         return ResponseEntity.noContent().build();
     }
}
