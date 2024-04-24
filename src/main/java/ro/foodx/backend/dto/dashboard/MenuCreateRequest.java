package ro.foodx.backend.dto.dashboard;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.foodx.backend.model.store.BagSize;
import ro.foodx.backend.model.user.UserRole;

@Getter
@Setter
@NoArgsConstructor
public class MenuCreateRequest {
    @NotNull(message = "{login_password_not_empty}")
    private String label;

    @NotNull(message = "{login_password_not_empty}")
    private String key;

    @NotNull(message = "{login_password_not_empty}")
    private String icon;

    @NotNull(message = "{login_password_not_empty}")
    private Boolean disabled;

    @NotNull(message = "{login_email_not_empty}")
    private UserRole userRole;
}
