package ro.foodx.backend.service.Impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateResponse;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.dto.store.StoreEditResponse;
import ro.foodx.backend.mapper.StoreMapper;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.service.StoreService;
import ro.foodx.backend.service.StoreValidationService;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final StoreValidationService storeValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    private static final String STORE_CREATION_SUCCESSFUL = "store_creation_successful";


    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }

    @Override
    public StoreCreateResponse saveStore(StoreCreateRequest storeCreateRequest) {

        storeValidationService.validateUser(storeCreateRequest);

        final Store store = StoreMapper.INSTANCE.convertToStore(storeCreateRequest);

        storeRepository.save(store);

        final String storeName = storeCreateRequest.getStoreName();
        final String storeSuccessMessage = generalMessageAccessor.getMessage(null, STORE_CREATION_SUCCESSFUL, storeName);

        log.info("{} registered successfully!", storeName);

        return new StoreCreateResponse(storeSuccessMessage);

    }

    @Override
    public StoreEditResponse editStore(StoreEditRequest storeEditRequest, Long id, String token) {
        storeValidationService.validateOwner(storeEditRequest, id, token);

        final Store store = StoreMapper.INSTANCE.convertToStore(storeEditRequest);

        final String storeName = storeEditRequest.getStoreName();
        final String storeSuccessMessage = generalMessageAccessor.getMessage(null, STORE_CREATION_SUCCESSFUL, storeName);

        log.info("{} registered successfully!", storeName);

        storeRepository.save(store);
        return new StoreEditResponse(storeSuccessMessage);
    }



    @Override
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

}
