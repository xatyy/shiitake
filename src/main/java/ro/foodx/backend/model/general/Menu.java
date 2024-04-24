package ro.foodx.backend.model.general;


import jakarta.persistence.*;
import lombok.*;
import ro.foodx.backend.model.user.UserRole;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_menuitem")
public class Menu {
    @Id
    @GeneratedValue
    private Long id;

    private String key;

    private String label;

    private String icon;

    private Boolean disabled;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
