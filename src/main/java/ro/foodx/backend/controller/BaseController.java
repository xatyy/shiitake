package ro.foodx.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ro.foodx.backend.model.user.UserRole;
import ro.foodx.backend.security.service.UserService;
import ro.foodx.backend.security.utils.SecurityConstants;
import ro.foodx.backend.utils.CookieUtils;

@Component
public abstract class BaseController {

    protected final UserService userService;

    public BaseController(UserService userService) {
        this.userService = userService;
    }

    protected ResponseEntity<?> handleJwtToken(HttpServletRequest request) {
        String jwtToken = CookieUtils.getJwtTokenFromCookies(request, SecurityConstants.COOKIE_NAME);
        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(jwtToken);
    }

    protected UserRole getUserRoleByToken(String jwtToken) {
        return userService.getRoleByToken(jwtToken);
    }
}
