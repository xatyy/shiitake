package ro.foodx.backend.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>, PagingAndSortingRepository<Product, Long> {

    Page<Product> findByStore_Id(Long store_id, Pageable pageable);



    Product findOneById(Long id);

    List<Product> findByStore_id(Long storeId);
}
