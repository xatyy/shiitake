package ro.foodx.backend.dto.reservation;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.foodx.backend.model.reservation.ReservationStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEditRequest {

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
}
