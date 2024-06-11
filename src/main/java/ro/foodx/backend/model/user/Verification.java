package ro.foodx.backend.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;

    private Long timestamp;


}
