package ro.foodx.backend.service;

import ro.foodx.backend.dto.category.CategoryCreateRequest;
import ro.foodx.backend.dto.category.CategoryCreateResponse;
import ro.foodx.backend.dto.dashboard.MenuCreateRequest;
import ro.foodx.backend.dto.dashboard.MenuCreateResponse;
import ro.foodx.backend.model.general.Menu;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.user.UserRole;

import java.util.List;

public interface GeneralService {
    List<Menu> getMenuByRole(UserRole userRole);

    MenuCreateResponse saveMenu(MenuCreateRequest menuCreateRequest, String token);
}
