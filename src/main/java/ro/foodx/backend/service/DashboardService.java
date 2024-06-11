package ro.foodx.backend.service;

import ro.foodx.backend.dto.dashboard.StoreResponse;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.store.DenyReason;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface DashboardService {
    StoreResponse storeRequestConfirm(Long storeId, String token);
    StoreResponse storeRequestDeny(Long storeId, DenyReason denyReason, String denyDetails, String token);
    Store storeView(Long storeId, String token);
    List<Product> storeProducts(Long storeId, String token);
    List<Reservation> storeReservation(Long storeId, String token);

    List<Reservation> getLastWeekReservations(Long storeId, String token);

    Path generateCsvFromReservations(List<Reservation> reservations) throws IOException;
}
