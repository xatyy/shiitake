package ro.foodx.backend.service;

import ro.foodx.backend.dto.category.CategoryCreateRequest;
import ro.foodx.backend.dto.category.CategoryCreateResponse;
import ro.foodx.backend.dto.category.CategoryEditRequest;
import ro.foodx.backend.dto.category.CategoryEditResponse;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.model.store.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoriesByStoreId(Long sellerId);

    CategoryCreateResponse saveCategory(CategoryCreateRequest categoryCreateRequest, Long storeId , String token);

    CategoryEditResponse editCategory(CategoryEditRequest categoryEditRequest, Long storeId, Long categoryId, String token);
}
