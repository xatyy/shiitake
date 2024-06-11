package ro.foodx.backend.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ReservationItemsRequest {
    @NotNull
    private Long productId;

    @NotNull
    private Long quantity;
}
