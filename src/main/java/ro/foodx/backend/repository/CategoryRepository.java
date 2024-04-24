package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.store.Category;
import ro.foodx.backend.model.store.Product;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByStore_Id(Long store_id);
}
