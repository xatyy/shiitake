package ro.foodx.backend.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.foodx.backend.dto.dashboard.MenuCreateRequest;
import ro.foodx.backend.dto.dashboard.MenuCreateResponse;
import ro.foodx.backend.exceptions.RegistrationException;
import ro.foodx.backend.mapper.GeneralMapper;
import ro.foodx.backend.model.general.Menu;
import ro.foodx.backend.model.user.UserRole;
import ro.foodx.backend.repository.MenuRepository;
import ro.foodx.backend.service.GeneralService;
import ro.foodx.backend.service.UserValidationService;
import ro.foodx.backend.utils.ExceptionMessageAccessor;
import ro.foodx.backend.utils.GeneralMessageAccessor;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    public final MenuRepository menuRepository;
    private final UserValidationService userValidationService;
    private final GeneralMessageAccessor generalMessageAccessor;
    private static final String USER_NOT_ALLOWED = "user_not_allowed";
    private static final String MENU_CREATION_SUCCESSFUL = "menu_creation_successful";

    private final ExceptionMessageAccessor exceptionMessageAccessor;


    @Override
    public List<Menu> getMenuByRole(UserRole userRole){
        return menuRepository.findByUserRole(userRole);
    }

    @Override
    public MenuCreateResponse saveMenu(MenuCreateRequest menuCreateRequest, String token) {

        if(!userValidationService.isAdmin(token)) {
            log.warn("Regular tried creating menu item");

            final String existsEmail = exceptionMessageAccessor.getMessage(null, USER_NOT_ALLOWED);
            throw new RegistrationException(existsEmail);
        }


        final Menu menu = GeneralMapper.INSTANCE.convertToMenu(menuCreateRequest);

        menuRepository.save(menu);

        final String menuName = menuCreateRequest.getLabel();
        final String menuSuccessMessage = generalMessageAccessor.getMessage(null, MENU_CREATION_SUCCESSFUL, menuName);

        log.info("{} created successfully!", menuName);

        return new MenuCreateResponse(menuSuccessMessage);
    }
}
