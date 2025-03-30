package Spring.Visit.NotificationModule.dtos;

import Spring.Visit.NotificationModule.Notification;
import Spring.Visit.NotificationModule.enums.NotificationReceiver;
import Spring.Visit.NotificationModule.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationRequestDTO {
    private Long receiverId;
    private String message;
    private NotificationType type;
    private NotificationReceiver sentTo;

    public static NotificationRequestDTO toNotificationRequestDTO(Notification notification){
        return new NotificationRequestDTO(
                notification.getReceiver().getId(),
                notification.getMessage(),
                notification.getType(),
                notification.getSentTo()
        );
    }
}
