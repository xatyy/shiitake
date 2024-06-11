package ro.foodx.backend.dto.dashboard;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.foodx.backend.model.store.DenyReason;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDeny {
    @NotNull(message = "{login_password_not_empty}")
    private Long storeId;

    @NotNull(message = "{login_password_not_empty}")
    private String denyDetails;

    @NotNull(message = "{login_password_not_empty}")
    private DenyReason denyReason;
}
