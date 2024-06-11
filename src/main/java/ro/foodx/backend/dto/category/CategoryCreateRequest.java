package ro.foodx.backend.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryCreateRequest {
    @NotNull(message = "{login_password_not_empty}")
    private String categoryName;
}
