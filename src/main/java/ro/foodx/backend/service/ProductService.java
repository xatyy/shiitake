package ro.foodx.backend.service;

import org.springframework.data.domain.Page;
import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.dto.product.ProductEditResponse;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.dto.store.StoreEditResponse;
import ro.foodx.backend.model.store.Product;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

public interface ProductService {
    Page<Product> getProductsByStoreId(Long sellerId, int pageNum, int pageSize);

    Page<Product> getProductsAsOwner(String token, Pageable pageable);

    Page<Product> getProductsFilteredAsOwner(String token, Map<String, String> filters, Pageable pageable);

    ProductCreateResponse saveProduct(ProductCreateRequest productCreateRequest, Long storeId , String token);

    ProductEditResponse editProduct(ProductEditRequest productEditRequest, Long storeId, Long productId, String token);
}
