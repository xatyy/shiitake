package ro.foodx.backend.model;


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
@Table(name = "_reviews")
public class Review {
    @Id
    @GeneratedValue
    private Long reviewId;

    private Long userId;

    private Long storeId;

    private Long productId;

    private int rating;
}
