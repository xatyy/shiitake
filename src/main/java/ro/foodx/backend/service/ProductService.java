package ro.foodx.backend.service;

import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.model.store.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProductsByStoreId(Long sellerId);

    ProductCreateResponse saveProduct(ProductCreateRequest productCreateRequest, Long storeId , String token);
}
