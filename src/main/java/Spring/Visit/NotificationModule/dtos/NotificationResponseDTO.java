package Spring.Visit.NotificationModule.dtos;

import Spring.Visit.NotificationModule.Notification;
import Spring.Visit.NotificationModule.enums.NotificationReceiver;
import Spring.Visit.NotificationModule.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private Long receiverId;
    private String message;
    private NotificationType type;
    private NotificationReceiver sentTo;
    private LocalDateTime sentAt;

    public static NotificationResponseDTO toNotificationResponseDTO(Notification notification){
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getReceiver().getId(),
                notification.getMessage(),
                notification.getType(),
                notification.getSentTo(),
                notification.getSentAt()
        );
    }
}
