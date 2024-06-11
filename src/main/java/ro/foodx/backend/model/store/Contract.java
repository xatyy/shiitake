package ro.foodx.backend.model.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ro.foodx.backend.model.user.User;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_contracts")
public class Contract {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String unsignedContractLink;

    private String signedContractLink;

    private Long signedTimeStamp;

    private Boolean contractRead;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    @JsonIgnore
    private Store store;
}
