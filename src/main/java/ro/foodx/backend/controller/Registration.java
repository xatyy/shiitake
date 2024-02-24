package ro.foodx.backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.security.dto.RegistrationRequest;
import ro.foodx.backend.security.dto.RegistrationResponse;
import ro.foodx.backend.security.service.UserService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class Registration {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> registrationRequest(@Valid @RequestBody RegistrationRequest registrationRequest) {

        final RegistrationResponse registrationResponse = userService.registration(registrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }
}
