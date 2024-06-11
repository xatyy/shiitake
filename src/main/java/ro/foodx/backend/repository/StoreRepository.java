package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store>, PagingAndSortingRepository<Store, Long> {
    boolean existsByUser_Id(UUID sellerId);

    Store findOneById(Long id);

    Store findOneByUser_Id(UUID sellerId);
}
