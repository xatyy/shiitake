package ro.foodx.backend.dto.dashboard;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String slug;

    @NotNull(message = "{login_password_not_empty}")
    private String icon;

    @NotNull(message = "{login_password_not_empty}")
    private Boolean disabled;

    @NotNull(message = "{login_email_not_empty}")
    private UserRole userRole;
}
