package ro.foodx.backend.model.store;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_category")
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private Long storeId;
    private String categoryName;
}
