package ro.foodx.backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.security.dto.LoginRequest;
import ro.foodx.backend.security.dto.LoginResponse;
import ro.foodx.backend.security.jwt.JwtTokenService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class Login {

    private final JwtTokenService jwtTokenService;

    @PostMapping
    public ResponseEntity<LoginResponse> loginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }


}
