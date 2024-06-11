package ro.foodx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ro.foodx.backend.model.reservation.Reservation;
import ro.foodx.backend.model.reservation.ReservationStatus;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;

import java.util.Date;
import java.util.List;
import java.util.UUID;



public interface ReservationRepository extends JpaRepository<Reservation, UUID>, JpaSpecificationExecutor<Reservation>, PagingAndSortingRepository<Reservation, UUID> {
    List<Reservation> findByStore_Id(Long store_id);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.store = :store AND r.reservationStatus IN :statuses AND r.reservationTimestamp BETWEEN :start AND :end")
    long countReservationsByStatusForDay(@Param("store") Store store, @Param("statuses") List<ReservationStatusGet> statuses, @Param("start") long start, @Param("end") long end);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.store = :store AND r.reservationTimestamp BETWEEN :startOfDay AND :endOfDay")
    long countReservationsForDay(@Param("store") Store store, @Param("startOfDay") long startOfDay, @Param("endOfDay") long endOfDay);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.store = :store AND r.reservationTimestamp BETWEEN :startOfMonth AND :endOfMonth")
    long countReservationsForMonth(@Param("store") Store store, @Param("startOfMonth") long startOfMonth, @Param("endOfMonth") long endOfMonth);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.store = :store")
    long countReservationsSinceRegistration(@Param("store") Store store);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.store.id = :storeId AND r.reservationTimestamp BETWEEN :start AND :end")
    int countByStoreIdAndReservationTimestampBetween(@Param("storeId") Long storeId, @Param("start") long start, @Param("end") long end);


    @Query("SELECT r FROM Reservation r WHERE r.store.id = :storeId AND CAST(r.reservationTimestamp AS long) >= :timestamp")
    List<Reservation> findByStoreIdAndReservationTimestampAfter(
            @Param("storeId") Long storeId,
            @Param("timestamp") String timestamp
    );

    @Query("SELECT r FROM Reservation r WHERE r.store.id = :storeId AND r.reservationTimestamp BETWEEN :start AND :end")
    List<Reservation> findByStoreIdAndReservationTimestampBetween(
            @Param("storeId") Long storeId,
            @Param("start") String startTimestamp,
            @Param("end") String endTimestamp
    );

    Reservation findOneById(UUID id);
}
