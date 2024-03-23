package ro.foodx.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.store.StoreCreateResponse;
import ro.foodx.backend.mapper.StoreMapper;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.repository.ProductRepository;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.repository.UserRepository;
import ro.foodx.backend.service.ProductService;
import ro.foodx.backend.service.StoreValidationService;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public final ProductRepository productRepository;

    private final StoreValidationService storeValidationService;

    private final StoreRepository storeRepository;

    private final GeneralMessageAccessor generalMessageAccessor;

    private static final String PRODUCT_CREATION_SUCCESSFUL = "product_creation_successful";

    @Override
    public List<Product> getProductsByStoreId(Long storeId) {
        return productRepository.findByStore_Id(storeId);
    }

    @Override
    public ProductCreateResponse saveProduct(ProductCreateRequest productCreateRequest, Long storeId, String token) {

        storeValidationService.validateOwner(storeId, token);

        final Store ownerStore;
        ownerStore = storeRepository.findOneById(productCreateRequest.getStoreId());

        final Product product = StoreMapper.INSTANCE.converToProduct(productCreateRequest);
        product.setStore(ownerStore);
        productRepository.save(product);

        final String productName = productCreateRequest.getProductName();
        final String productSuccessMessage = generalMessageAccessor.getMessage(null, PRODUCT_CREATION_SUCCESSFUL, productName);

        log.info("{} added successfully!", productName);

        return new ProductCreateResponse(productSuccessMessage);

    }
}
