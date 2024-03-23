package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByUser_Id(Long sellerId);

    Store findOneById(Long id);
}
