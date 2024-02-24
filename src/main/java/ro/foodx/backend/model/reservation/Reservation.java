package ro.foodx.backend.model.reservation;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_reservations")
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    private Long productId;

    private Long customerId;

    private Long storeId;

    private String reservationTimestamp;

    private String pickUpTimeLimit;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private Double reservationPrice;

}
