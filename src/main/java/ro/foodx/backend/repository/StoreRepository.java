package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.store.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsById(Long sellerId);

}
