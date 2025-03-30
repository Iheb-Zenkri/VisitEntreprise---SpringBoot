package Spring.Visit.NotificationModule;

import Spring.Visit.NotificationModule.dtos.NotificationRequestDTO;
import Spring.Visit.NotificationModule.dtos.NotificationResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationRequestDTO notificationRequestDTO) {
        NotificationResponseDTO notificationResponseDTO = notificationService.createNotification(
                    notificationRequestDTO.getReceiverId(),
                    notificationRequestDTO.getMessage(),
                    notificationRequestDTO.getType(),
                    notificationRequestDTO.getSentTo()
            );

        return ResponseEntity.status(HttpStatus.CREATED).body(notificationResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getAllUserNotifications(@PathVariable Long userId) {
            List<Notification> notifications = notificationService.getAllUserNotifications(userId);
            return ResponseEntity.ok(notifications.stream().map(NotificationResponseDTO::toNotificationResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
            Notification notification = notificationService.getNotificationById(id);
            return ResponseEntity.ok(NotificationResponseDTO.toNotificationResponseDTO(notification));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok("Notification deleted successfully");
    }
}
