package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByUser_Id(UUID sellerId);

    Store findOneById(Long id);
}
