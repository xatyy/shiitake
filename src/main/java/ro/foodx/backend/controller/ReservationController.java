package ro.foodx.backend.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.dto.reservation.ReservationCreateRequest;
import ro.foodx.backend.dto.reservation.ReservationCreateResponse;
import ro.foodx.backend.service.ReservationService;

@RestController
@RequestMapping("/api/v1/store")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/{storeId}/reservation")
    public ResponseEntity<ReservationCreateResponse> addReservation(@Valid @RequestBody ReservationCreateRequest reservationCreateRequest, @PathVariable Long storeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        final ReservationCreateResponse reservationCreateResponse = reservationService.addReservation(reservationCreateRequest, storeId, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationCreateResponse);
    }
}
