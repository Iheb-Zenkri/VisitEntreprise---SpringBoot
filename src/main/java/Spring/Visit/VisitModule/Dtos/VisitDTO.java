package Spring.Visit.VisitModule.Dtos;

import Spring.Visit.UserModule.dto.TeacherDTO;
import Spring.Visit.VisitModule.entities.Visit;
import Spring.Visit.VisitModule.enums.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitDTO {
    private Long id;
    private LocalDateTime visitDate;
    private String location;
    private VisitStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TeacherDTO responsible;
    private CompanyDTO company ;
    private int feedbacksCount;

    public static VisitDTO toVisitDTO(Visit visit){
        return new VisitDTO(
                visit.getId(),
                visit.getVisitDate(),
                visit.getLocation(),
                visit.getStatus(),
                visit.getNotes(),
                visit.getCreatedAt(),
                visit.getUpdatedAt(),
                visit.getResponsible() == null ? new TeacherDTO() : TeacherDTO.toTeacherDTO(visit.getResponsible()),
                visit.getCompany() == null ? new CompanyDTO() : CompanyDTO.toCompanyDTO(visit.getCompany()),
                visit.getFeedbacks().size()
        );
    }
}
