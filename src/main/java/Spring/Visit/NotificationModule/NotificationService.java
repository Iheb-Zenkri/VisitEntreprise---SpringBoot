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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

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
        Notification returnedNotification = new Notification();

        if(sentTo.equals(NotificationReceiver.TEACHER) || sentTo.equals(NotificationReceiver.STUDENT)){
            User user = userRepository.findById(receiverId)
                    .orElseThrow(() -> new UserNotFoundException("User with id "+receiverId+" not Found"));
            returnedNotification.setReceiver(user);
            returnedNotification.setMessage(message);
            returnedNotification.setType(type);
            returnedNotification.setSentAt(LocalDateTime.now());
            returnedNotification.setSentTo(sentTo);
            NotificationResponseDTO notificationResponseDTO = NotificationResponseDTO.toNotificationResponseDTO(notificationRepository.save(returnedNotification));
            messagingTemplate.convertAndSend( "/topic/notifications/"+user.getId().toString(), notificationResponseDTO);
            System.out.println(receiverId.toString());
            return notificationResponseDTO ;
        }
        else if(sentTo.equals(NotificationReceiver.STUDENT_GROUP)){
            List<User> students = groupRepository.getReferenceById(receiverId).getStudents().stream().map(student -> (User) student).toList() ;
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

            messagingTemplate.convertAndSend( "/topic/notifications", returnedNotification);

            return NotificationResponseDTO.toNotificationResponseDTO(returnedNotification) ;
        }else {
            /*
            *   that block is for managing the notifications sent to all concerned users in a visit
            *   it will filter the teachers and students belong to a visit and create for each
            *   separate notification
             */

            return NotificationResponseDTO.toNotificationResponseDTO(returnedNotification) ;
        }
    }

    public List<Notification> getAllUserNotifications(Long userId) {
        return notificationRepository.findByReceiverId(userId);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Notification with id "+id+" not found"));
    }

    public void deleteNotification(Long id) {
        if(!notificationRepository.existsById(id)){
            throw new ObjectNotFoundException("Notification with id "+id+" not found");
        }
        notificationRepository.deleteById(id);
    }



}
