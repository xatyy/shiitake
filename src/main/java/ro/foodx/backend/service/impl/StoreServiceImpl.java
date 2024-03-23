package ro.foodx.backend.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateResponse;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.dto.store.StoreEditResponse;
import ro.foodx.backend.mapper.StoreMapper;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.store.StoreType;
import ro.foodx.backend.model.user.User;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final StoreValidationService storeValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    private static final String STORE_CREATION_SUCCESSFUL = "store_creation_successful";
    private static final String STORE_MODIFIED_SUCCESSFUL = "store_modified_successful";


    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }

    @Override
    public StoreCreateResponse saveStore(StoreCreateRequest storeCreateRequest, String token) {

        storeValidationService.validateUser(storeCreateRequest, token);

        final User ownerUser;
        ownerUser = userRepository.findOneById(storeCreateRequest.getSellerId());


        final Store store = StoreMapper.INSTANCE.convertToStore(storeCreateRequest);
        store.setUser(ownerUser);
        storeRepository.save(store);

        final String storeName = storeCreateRequest.getStoreName();
        final String storeSuccessMessage = generalMessageAccessor.getMessage(null, STORE_CREATION_SUCCESSFUL, storeName);

        log.info("{} registered successfully!", storeName);

        return new StoreCreateResponse(storeSuccessMessage);

    }


    @Override
    @Transactional
    public StoreEditResponse editStore(StoreEditRequest storeEditRequest, Long id, String token) {
        storeValidationService.validateOwner(id, token);

        final Store store = StoreMapper.INSTANCE.convertToStore(storeEditRequest);

        final String storeSuccessMessage = generalMessageAccessor.getMessage(null, STORE_MODIFIED_SUCCESSFUL, id);

        log.info("Store with id {} modified successfully!", id);

        Optional<Store> storeToEdit = storeRepository.findById(id);

        String newStoreName = store.getStoreName();
        String newDescription = store.getDescription();
        String newAddress = store.getAddress();
        String newPhoneNumber = store.getPhoneNumber();
        StoreType newCuisineType = store.getCuisineType();
        String newProfilePicURL = store.getProfilePicURL();
        String newCoverPicURL = store.getProfilePicURL();

        storeToEdit.ifPresent(value -> {
            if(newStoreName != null) {
                value.setStoreName(newStoreName);
            }
            if(newDescription != null){
                value.setDescription(newDescription);
            }
            if(newAddress != null){
                value.setAddress(newAddress);
            }
            if(newPhoneNumber != null){
                value.setPhoneNumber(newPhoneNumber);
            }
            if(newCuisineType != null){
                value.setCuisineType(newCuisineType);
            }
            if(newProfilePicURL != null){
                value.setProfilePicURL(newProfilePicURL);
            }
            if(newCoverPicURL != null) {
                value.setCoverPicURL(newProfilePicURL);
            }
        });

        return new StoreEditResponse(storeSuccessMessage);
    }



    @Override
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

}
