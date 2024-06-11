package ro.foodx.backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateResponse;
import ro.foodx.backend.security.dto.VerificationRequest;
import ro.foodx.backend.security.dto.VerificationResponse;
import ro.foodx.backend.security.service.VerificationService;
import ro.foodx.backend.service.StoreService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verify")
public class VerificationController {

    private final VerificationService verificationService;

    @GetMapping("/{id}")
    public ResponseEntity<VerificationResponse> verifyAccount(@PathVariable UUID id) {

       final VerificationResponse verificationResponse = verificationService.verifyAccount(id);

      return ResponseEntity.status(HttpStatus.CREATED).body(verificationResponse);
    }
    @PostMapping("/requestCode")
    public ResponseEntity<VerificationResponse> verifyAccount(@Valid @RequestBody VerificationRequest verificationRequest) {

        final VerificationResponse verificationResponse = verificationService.getNewCode(verificationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(verificationResponse);
    }

}
