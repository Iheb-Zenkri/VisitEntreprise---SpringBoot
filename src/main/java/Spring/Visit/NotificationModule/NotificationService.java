package Spring.Visit.NotificationModule;

import Spring.Visit.NotificationModule.dtos.NotificationResponseDTO;
import Spring.Visit.NotificationModule.enums.NotificationReceiver;
import Spring.Visit.NotificationModule.enums.NotificationType;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.repositories.GroupRepository;
import Spring.Visit.UserModule.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, GroupRepository groupRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public NotificationResponseDTO createNotification(Long receiverId, String message, NotificationType type, NotificationReceiver sentTo) {
        logger.info("Creating notification for receiver ID: {}, message: '{}', type: {}, sentTo: {}", receiverId, message, type, sentTo);

        Notification returnedNotification = new Notification();

        if (sentTo.equals(NotificationReceiver.TEACHER) || sentTo.equals(NotificationReceiver.STUDENT)) {
            User user = userRepository.findById(receiverId)
                    .orElseThrow(() -> {
                        logger.error("User with ID {} not found", receiverId);
                        return new UserNotFoundException("User with id " + receiverId + " not found");
                    });

            returnedNotification.setReceiver(user);
            returnedNotification.setMessage(message);
            returnedNotification.setType(type);
            returnedNotification.setSentAt(LocalDateTime.now());
            returnedNotification.setSentTo(sentTo);

            NotificationResponseDTO notificationResponseDTO = NotificationResponseDTO.toNotificationResponseDTO(notificationRepository.save(returnedNotification));

            messagingTemplate.convertAndSend("/topic/notifications/" + user.getId().toString(), notificationResponseDTO);
            logger.info("Notification sent to user ID: {}", receiverId);

            return notificationResponseDTO;
        } else if (sentTo.equals(NotificationReceiver.STUDENT_GROUP)) {
            List<User> students = groupRepository.getReferenceById(receiverId).getStudents().stream().map(student -> (User) student).toList();
            List<Notification> notifications = students.stream().map(user -> {
                Notification notification = new Notification();
                notification.setReceiver(user);
                notification.setMessage(message);
                notification.setType(type);
                notification.setSentTo(NotificationReceiver.STUDENT);
                notification.setSentAt(LocalDateTime.now());
                return notification;
            }).toList();

            returnedNotification = notificationRepository.saveAll(notifications).get(0);

            messagingTemplate.convertAndSend("/topic/notifications", returnedNotification);

            logger.info("Notifications sent to student group with receiver ID: {}", receiverId);

            return NotificationResponseDTO.toNotificationResponseDTO(returnedNotification);
        } else {
            // Block for handling notifications for all concerned users in a visit
            logger.info("Notification created for visit-related receivers");
            return NotificationResponseDTO.toNotificationResponseDTO(returnedNotification);
        }
    }

    public List<Notification> getAllUserNotifications(Long userId) {
        logger.info("Fetching all notifications for user ID: {}", userId);
        return notificationRepository.findByReceiverId(userId);
    }

    public Notification getNotificationById(Long id) {
        logger.info("Fetching notification with ID: {}", id);
        return notificationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Notification with ID {} not found", id);
                    return new ObjectNotFoundException("Notification with id " + id + " not found");
                });
    }

    public void deleteNotification(Long id) {
        logger.info("Deleting notification with ID: {}", id);

        if (!notificationRepository.existsById(id)) {
            logger.error("Notification with ID {} not found for deletion", id);
            throw new ObjectNotFoundException("Notification with id " + id + " not found");
        }

        notificationRepository.deleteById(id);
        logger.info("Notification with ID {} deleted successfully", id);
    }
}
