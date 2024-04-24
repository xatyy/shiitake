package ro.foodx.backend.dto.category;

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

@Getter
@Setter
@NoArgsConstructor
public class CategoryCreateRequest {
    @NotNull(message = "{login_password_not_empty}")
    private String categoryName;
}
