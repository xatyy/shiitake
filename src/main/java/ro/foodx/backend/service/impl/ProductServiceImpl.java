package ro.foodx.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.dto.product.ProductEditResponse;
import ro.foodx.backend.dto.store.StoreEditResponse;
import ro.foodx.backend.mapper.StoreMapper;
import ro.foodx.backend.model.store.*;
import ro.foodx.backend.repository.ProductRepository;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.service.ProductService;
import ro.foodx.backend.service.StoreValidationService;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public final ProductRepository productRepository;

    private final StoreValidationService storeValidationService;

    private final StoreRepository storeRepository;

    private final GeneralMessageAccessor generalMessageAccessor;

    private static final String PRODUCT_CREATION_SUCCESSFUL = "product_creation_successful";

    private static final String PRODUCT_MODIFIED_SUCCESSFUL = "product_modified_successful";

    @Override
    public List<Product> getProductsByStoreId(Long storeId) {
        return productRepository.findByStore_Id(storeId);
    }

    @Override
    public ProductCreateResponse saveProduct(ProductCreateRequest productCreateRequest, Long storeId, String token) {

        storeValidationService.validateOwner(storeId, token);

        final Store ownerStore;
        ownerStore = storeRepository.findOneById(storeId);

        final Product product = StoreMapper.INSTANCE.converToProduct(productCreateRequest);
        product.setStore(ownerStore);

        log.info("{}",product.getIsBag());

        productRepository.save(product);

        final String productName = productCreateRequest.getProductName();
        final Boolean productBag = productCreateRequest.getIsBag();
        final String productSuccessMessage = generalMessageAccessor.getMessage(null, PRODUCT_CREATION_SUCCESSFUL, productName);

        log.info("{} added successfully! isBag? - {}", productName, productBag);

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
        Double newPrice = product.getPrice();
        String newPic = product.getProductImage();
        Boolean newIsBag = product.getIsBag();
        Boolean newIsPublished = product.getIsPublished();
        int newQuantity = product.getQuantity();
        BagSize newBagSize = product.getBagSize();
        int newCustomerRestriction = product.getCustomerRestriction();
        String newCollectStart = product.getCollectStart();
        String newCollectEnd = product.getCollectEnd();

        productToEdit.ifPresent(value -> {
            if(newProductName != null) {
                value.setProductName(newProductName);
            }
            if(newDescription != null){
                value.setProductDescription(newDescription);
            }
            if(newPrice != null){
                value.setPrice(newPrice);
            }
            if(newPic != null){
                value.setProductImage(newPic);
            }
            if(newIsBag != null){
                value.setIsBag(newIsBag);
            }
            if(newIsPublished != null){
                value.setIsPublished(newIsPublished);
            }
            if(newBagSize != null){
                value.setBagSize(newBagSize);
            }
            if(newCollectStart != null){
                value.setCollectStart(newCollectStart);
            }
            if(newCollectEnd != null) {
                value.setCollectEnd(newCollectEnd);
            }
            if(newQuantity != value.getQuantity()){
                value.setQuantity(newQuantity);
            }
            if(newCustomerRestriction != value.getCustomerRestriction()){
                value.setQuantity(newCustomerRestriction);
            }
        });

        return new ProductEditResponse(productSuccessMessage);
    }



}
