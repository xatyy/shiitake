package ro.foodx.backend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.dto.category.CategoryCreateRequest;
import ro.foodx.backend.dto.category.CategoryCreateResponse;
import ro.foodx.backend.dto.dashboard.MenuCreateRequest;
import ro.foodx.backend.dto.dashboard.MenuCreateResponse;
import ro.foodx.backend.model.general.Menu;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.UserRole;
import ro.foodx.backend.security.service.UserService;
import ro.foodx.backend.security.utils.SecurityConstants;
import ro.foodx.backend.service.GeneralService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CrossOrigin()
@RestController
@RequestMapping("/api/v1/dashboard")
public class Dashboard {

    private final GeneralService generalService;
    private final UserService userService;

    public Dashboard(GeneralService generalService, UserService userService) {
        this.generalService = generalService;
        this.userService = userService;
    }

    @GetMapping("/menu")
    public ResponseEntity<List<Menu>> getAllMenus( HttpServletRequest request) {
        String jwtToken = null;

        // Retrieve the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                jwtToken = jwtCookie.get().getValue();
            }
        }

        if (jwtToken == null) {
            // Handle the scenario when the token is not found in the cookie
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserRole role = userService.getRoleByToken(jwtToken);
        List<Menu> menus = generalService.getMenuByRole(role);
        return ResponseEntity.ok(menus);
    }

    @PostMapping("/menu")
    public ResponseEntity<MenuCreateResponse> addMenu(@Valid @RequestBody MenuCreateRequest menuCreateRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token ) {

        final MenuCreateResponse menuCreateResponse = generalService.saveMenu(menuCreateRequest, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(menuCreateResponse);
    }
}
