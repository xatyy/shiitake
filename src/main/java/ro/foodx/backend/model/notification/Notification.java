package ro.foodx.backend.model.notification;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_notifications")
public class Notification {
    @Id
    @GeneratedValue
    private Long notificationId;

    private Long userId;

    private String title;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Boolean isRead;

    private String timestamp;

    private Long orderId;

    private Long storeId;

}
