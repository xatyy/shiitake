package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStore_Id(Long store_id);

    Product findOneById(Long id);
}
