package ro.foodx.backend.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateResponse;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.dto.store.StoreEditResponse;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.security.dto.RegistrationResponse;
import ro.foodx.backend.service.StoreService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    @Secured("USER")
    public ResponseEntity<List<Store>> getAllStores() {
        List<Store> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        Optional<Store> store = storeService.getStoreById(id);

        return store.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StoreCreateResponse> addStore(@Valid @RequestBody StoreCreateRequest storeCreateRequest ) {

        final StoreCreateResponse storeCreateResponse = storeService.saveStore(storeCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeCreateResponse);
    }


    //TODO: Finalize implementing the edit procedure.
    /*
    @PatchMapping("/{id}")
    public ResponseEntity<StoreEditResponse> updateStore(@Valid @PathVariable Long id, @RequestBody StoreEditRequest storeEditRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        final StoreEditResponse storeEditResponse = storeService.editStore(storeEditRequest, id, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeEditResponse);
    }
*/


     @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
         storeService.deleteStore(id);
         return ResponseEntity.noContent().build();
     }
}
