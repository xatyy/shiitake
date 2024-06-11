package ro.foodx.backend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.dto.product.ProductEditResponse;
import ro.foodx.backend.dto.reservation.ReservationCreateRequest;
import ro.foodx.backend.dto.reservation.ReservationCreateResponse;
import ro.foodx.backend.dto.reservation.ReservationEditRequest;
import ro.foodx.backend.dto.reservation.ReservationEditResponse;
import ro.foodx.backend.security.utils.SecurityConstants;
import ro.foodx.backend.service.ReservationService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/store")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PatchMapping("/{storeId}/reservation/{id}")
    public ResponseEntity<ReservationEditResponse> updateReservationStatus(@Valid @PathVariable Long storeId, @PathVariable UUID id, @RequestBody ReservationEditRequest reservationEditRequest, HttpServletRequest request ) {

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


        final ReservationEditResponse reservationEditResponse = reservationService.editReservation(reservationEditRequest, storeId, id, jwtToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationEditResponse);
    }

    @GetMapping("/reservations/today")
    public long getReservationsForToday(@RequestParam Long storeId) {
        return reservationService.getReservationsForToday(storeId);
    }

    @GetMapping("/reservations/month")
    public long getReservationsForCurrentMonth(@RequestParam Long storeId) {
        return reservationService.getReservationsForCurrentMonth(storeId);
    }

    @GetMapping("/reservations/total")
    public long getTotalReservationsSinceRegistration(@RequestParam Long storeId) {
        return reservationService.getTotalReservationsSinceRegistration(storeId);
    }

    @PostMapping("/{storeId}/reservation")
    public ResponseEntity<ReservationCreateResponse> addReservation(@Valid @RequestBody ReservationCreateRequest reservationCreateRequest, @PathVariable Long storeId, HttpServletRequest request) {

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


        final ReservationCreateResponse reservationCreateResponse = reservationService.addReservation(reservationCreateRequest, storeId, jwtToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationCreateResponse);
    }
}
