package ro.foodx.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateResponse;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.dto.store.StoreEditResponse;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StoreService {

    List<Store> getAllStores();
    Optional<Store> getStoreById(Long id);
    StoreCreateResponse saveStore(StoreCreateRequest storeCreateRequest, String token);
    StoreEditResponse editStore(StoreEditRequest storeEditRequest, Long id, String token);
    void deleteStore(Long id);
    Page<Store> getStoresAsOwner(String token, Pageable pageable);
    Page<Store> getStoresFilteredAsOwner(String token, Map<String, String> filters, Pageable pageable);
}
