package Spring.Visit.VisitModule.Dtos;

import Spring.Visit.UserModule.dto.UserDTO;
import Spring.Visit.VisitModule.entities.Feedback;
import Spring.Visit.VisitModule.enums.FeedbackRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private Long Id ;
    private String feedbackRating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO user;

    public static FeedbackDTO toFeedBackDTO(Feedback feedback){
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getRating().name(),
                feedback.getComment(),
                feedback.getCreatedAt(),
                feedback.getUpdatedAt(),
                UserDTO.toUserDTO(feedback.getAddedBy())
        );
    }
}
