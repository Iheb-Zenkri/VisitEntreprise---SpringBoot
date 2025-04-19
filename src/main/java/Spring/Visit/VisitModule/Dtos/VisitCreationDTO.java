package Spring.Visit.VisitModule.Dtos;

import Spring.Visit.VisitModule.enums.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitCreationDTO {
    private Long companyId;
    private LocalDateTime visitDate;
    private VisitStatus status;
    private String notes;
}
