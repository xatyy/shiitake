package ro.foodx.backend.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.security.dto.LoginRequest;
import ro.foodx.backend.security.dto.LoginResponse;
import ro.foodx.backend.security.jwt.JwtTokenService;
import jakarta.servlet.http.Cookie;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class Login {

    private final JwtTokenService jwtTokenService;

    @PostMapping
    public ResponseEntity<LoginResponse> loginRequest(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);

        String token = loginResponse.getToken();

        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // For production
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponse);
    }


}
