package ro.foodx.backend.model.user;

import jakarta.persistence.*;
import lombok.*;
import ro.foodx.backend.model.store.Store;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

    private String phoneNumber;

    private Boolean isConfirmed;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(mappedBy = "user")
    private Store store;

}
