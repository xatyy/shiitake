package ro.foodx.backend.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReservationCreateRequest {
    @NotNull(message = "{login_email_not_empty}")
    private Long productId;

    @NotNull
    private Long quantity;

    private String specialInstructions;
}
