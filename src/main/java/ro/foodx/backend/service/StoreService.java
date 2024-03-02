package ro.foodx.backend.service;

import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateResponse;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.dto.store.StoreEditResponse;
import ro.foodx.backend.model.store.Store;
import java.util.List;
import java.util.Optional;

public interface StoreService {

    List<Store> getAllStores();
    Optional<Store> getStoreById(Long id);
    StoreCreateResponse saveStore(StoreCreateRequest storeCreateRequest);
    StoreEditResponse editStore(StoreEditRequest storeEditRequest, Long id, String token);
    void deleteStore(Long id);
}
