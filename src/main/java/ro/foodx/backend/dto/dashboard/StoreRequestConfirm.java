package ro.foodx.backend.dto.dashboard;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestConfirm {
    @NotNull(message = "{login_password_not_empty}")
    private Long storeId;
}
