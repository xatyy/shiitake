package ro.foodx.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.dto.product.ProductEditResponse;
import ro.foodx.backend.dto.reservation.ReservationCreateRequest;
import ro.foodx.backend.dto.reservation.ReservationCreateResponse;
import ro.foodx.backend.dto.reservation.ReservationEditRequest;
import ro.foodx.backend.dto.reservation.ReservationEditResponse;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.store.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReservationService {
    List<Reservation> getReservationsByStoreId(String token);

    Page<Reservation> getReservationsAsOwner(String token, Pageable pageable);

    Page<Reservation> getReservationsFilteredAsOwner(String token, Map<String, String> filters, Pageable pageable);

    ReservationCreateResponse addReservation(ReservationCreateRequest reservationCreateRequest, Long storeId , String token);

    ReservationEditResponse editReservation(ReservationEditRequest reservationEditRequest, Long storeId, UUID reservationId, String token);

    long getReservationsForToday(Long storeId);

    long getReservationsForCurrentMonth(Long storeId);

    long getTotalReservationsSinceRegistration(Long storeId);

    long getConfirmedOrPendingReservationsForToday(Long storeId);

}
