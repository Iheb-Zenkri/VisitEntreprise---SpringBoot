package Spring.Visit.VisitModule.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackCreationDTO {
    private Long userId;
    private Long visitId;
    private String Comment;
}
