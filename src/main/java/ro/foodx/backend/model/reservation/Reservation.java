package ro.foodx.backend.model.reservation;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;
import ro.foodx.backend.model.user.User;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name  = "store_id", referencedColumnName = "id")
    private Store store;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name  = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name  = "customer_id", referencedColumnName = "id")
    private User user;

    private String reservationTimestamp;

    private String specialInstructions;

    private long quantity;

   // @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String pickUpTimeLimit;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private Double reservationPrice;

}
