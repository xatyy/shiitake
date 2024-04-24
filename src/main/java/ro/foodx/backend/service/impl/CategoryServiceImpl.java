package ro.foodx.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.category.CategoryCreateRequest;
import ro.foodx.backend.dto.category.CategoryCreateResponse;
import ro.foodx.backend.dto.category.CategoryEditRequest;
import ro.foodx.backend.dto.category.CategoryEditResponse;
import ro.foodx.backend.mapper.StoreMapper;
import ro.foodx.backend.model.store.Category;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.repository.CategoryRepository;
import ro.foodx.backend.repository.StoreRepository;
import ro.foodx.backend.service.CategoryService;
import ro.foodx.backend.service.StoreValidationService;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    public final CategoryRepository categoryRepository;

    private final StoreValidationService storeValidationService;

    private final StoreRepository storeRepository;

    private final GeneralMessageAccessor generalMessageAccessor;

    private static final String CATEGORY_CREATION_SUCCESSFUL = "category_creation_successful";

    private static final String CATEGORY_MODIFIED_SUCCESSFUL = "category_modified_successful";
    @Override
    public List<Category> getCategoriesByStoreId(Long storeId) {
        return categoryRepository.findByStore_Id(storeId);
    }

    @Override
    public CategoryCreateResponse saveCategory(CategoryCreateRequest categoryCreateRequest, Long storeId, String token) {
        storeValidationService.validateOwner(storeId, token);

        final Store ownerStore;
        ownerStore = storeRepository.findOneById(storeId);

        final Category category = StoreMapper.INSTANCE.convertToCategory(categoryCreateRequest);
        category.setStore(ownerStore);

        categoryRepository.save(category);

        final String categoryName = categoryCreateRequest.getCategoryName();
        final String categorySuccessMessage = generalMessageAccessor.getMessage(null, CATEGORY_CREATION_SUCCESSFUL, categoryName);

        log.info("{} created successfully!", categoryName);

        return new CategoryCreateResponse(categorySuccessMessage);
    }

    @Override
    public CategoryEditResponse editCategory(CategoryEditRequest categoryEditRequest, Long storeId, Long categoryId, String token) {
        storeValidationService.validateOwner(storeId, token);

        final Category category = StoreMapper.INSTANCE.convertToCategory(categoryEditRequest);

        final String categorySuccessMessage = generalMessageAccessor.getMessage(null, CATEGORY_MODIFIED_SUCCESSFUL, categoryId);

        log.info("Category with id {} modified successfully!", category);

        Optional<Category> categoryToEdit = categoryRepository.findById(categoryId);

        String newCategoryName = category.getCategoryName();

        categoryToEdit.ifPresent(value -> {
            if(newCategoryName != null) {
                value.setCategoryName(newCategoryName);
            }
        });
        return new CategoryEditResponse(categorySuccessMessage);
    }
}
