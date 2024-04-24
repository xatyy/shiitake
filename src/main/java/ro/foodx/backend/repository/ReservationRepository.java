package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStore_Id(Long store_id);

    Reservation findOneById(UUID id);
}
