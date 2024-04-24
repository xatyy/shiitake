package ro.foodx.backend.service;

import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.product.ProductCreateResponse;
import ro.foodx.backend.dto.product.ProductEditRequest;
import ro.foodx.backend.dto.product.ProductEditResponse;
import ro.foodx.backend.dto.reservation.ReservationCreateRequest;
import ro.foodx.backend.dto.reservation.ReservationCreateResponse;
import ro.foodx.backend.model.reservation.Reservation;

import java.util.List;

public interface ReservationService {
    List<Reservation> getReservationsByStoreId(Long sellerId);

    ReservationCreateResponse addReservation(ReservationCreateRequest reservationCreateRequest, Long storeId , String token);

    ProductEditResponse editProduct(ProductEditRequest productEditRequest, Long storeId, Long productId, String token);
}
