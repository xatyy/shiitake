package ro.foodx.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.dto.product.ProductEditResponse;
import ro.foodx.backend.mapper.StoreMapper;
import ro.foodx.backend.model.store.*;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.repository.ProductRepository;
import ro.foodx.backend.repository.ProductSpecifications;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.service.ProductService;
import ro.foodx.backend.service.StoreValidationService;
import ro.foodx.backend.service.UserValidationService;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public final ProductRepository productRepository;

    private final StoreValidationService storeValidationService;

    private final StoreRepository storeRepository;

    private final GeneralMessageAccessor generalMessageAccessor;

    private final UserValidationService userValidationService;

    private static final String PRODUCT_CREATION_SUCCESSFUL = "product_creation_successful";

    private static final String PRODUCT_MODIFIED_SUCCESSFUL = "product_modified_successful";

    @Override
    public Page<Product> getProductsByStoreId(Long storeId, int pageNum, int pageSize) {

        return productRepository.findByStore_Id(storeId, PageRequest.of(pageNum, pageSize));
    }
    @Override
    public Page<Product> getProductsFilteredAsOwner(String token, Map<String, String> filters, Pageable pageable) {
        User user = userValidationService.returnUserFromToken(token);
        UUID userId = user.getId();  // Assuming you're using UUID for user IDs
        Store store = storeRepository.findOneByUser_Id(userId);  // Ensure this method correctly fetches the store
        Long storeId = store.getId();

        Specification<Product> spec = ProductSpecifications.byFilter(storeId, filters);  // Use the combined specification
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Product> getProductsAsOwner(String token, Pageable pageable) {
        User user = userValidationService.returnUserFromToken(token);
        UUID userToken = user.getId();
        Store store = storeRepository.findOneByUser_Id(userToken);
        Long storeId = store.getId();
        return productRepository.findByStore_Id(storeId, pageable);
    }

    @Override
    public ProductCreateResponse saveProduct(ProductCreateRequest productCreateRequest, Long storeId, String token) {

        storeValidationService.validateOwner(storeId, token);

        final Store ownerStore;
        ownerStore = storeRepository.findOneById(storeId);

        final Product product = StoreMapper.INSTANCE.converToProduct(productCreateRequest);
        product.setStore(ownerStore);


        productRepository.save(product);

        final String productName = productCreateRequest.getProductName();
        final String productSuccessMessage = generalMessageAccessor.getMessage(null, PRODUCT_CREATION_SUCCESSFUL, productName);

        log.info("{} added successfully!", productName);

        return new ProductCreateResponse(productSuccessMessage);

    }

    @Override
    @Transactional
    public ProductEditResponse editProduct(ProductEditRequest productEditRequest, Long storeId, Long productId, String token) {
        storeValidationService.validateOwner(storeId, token);

        final Product product = StoreMapper.INSTANCE.converToProduct(productEditRequest);

        final String productSuccessMessage = generalMessageAccessor.getMessage(null, PRODUCT_MODIFIED_SUCCESSFUL, productId);

        log.info("Product with id {} modified successfully!", productId);

        Optional<Product> productToEdit = productRepository.findById(productId);

        String newProductName = product.getProductName();
        String newDescription = product.getProductDescription();
        Double newInitialPrice = product.getInitialPrice();
        Double newFinalPrice = product.getFinalPrice();
        String newPic = product.getProductImage();
        Boolean newIsPublished = product.getIsPublished();
        int newProductQuantity = product.getProductQuantity();
        int newProductWeight = product.getProductWeight();
        List<String> newDietary = product.getDietary();
        List<String> newAllergens = product.getAllergens();
        BagType newBagType = product.getBagType();
        String newCollectStart = product.getCollectStart();
        String newCollectEnd = product.getCollectEnd();

        productToEdit.ifPresent(value -> {
            if(newProductName != null) {
                value.setProductName(newProductName);
            }
            if(newDescription != null){
                value.setProductDescription(newDescription);
            }
            if(newInitialPrice != null){
                value.setInitialPrice(newInitialPrice);
            }
            if(newFinalPrice != null){
                value.setInitialPrice(newFinalPrice);
            }
            if(newPic != null){
                value.setProductImage(newPic);
            }
            if(newIsPublished != null){
                value.setIsPublished(newIsPublished);
            }
            if(newBagType != null){
                value.setBagType(newBagType);
            }
            if(newCollectStart != null){
                value.setCollectStart(newCollectStart);
            }
            if(newCollectEnd != null) {
                value.setCollectEnd(newCollectEnd);
            }
            if(newProductQuantity != value.getProductQuantity()){
                value.setProductQuantity(newProductQuantity);
            }
            if(newProductWeight != value.getProductWeight()){
                value.setProductWeight(newProductWeight);
            }
            if(!newDietary.isEmpty()) {
                value.setDietary(newDietary);
            }
            if(!newAllergens.isEmpty()) {
                value.setAllergens(newAllergens);
            }
        });

        return new ProductEditResponse(productSuccessMessage);
    }



}
