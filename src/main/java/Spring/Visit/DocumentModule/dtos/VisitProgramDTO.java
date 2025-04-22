package Spring.Visit.DocumentModule.dtos;

import Spring.Visit.DocumentModule.entities.VisitProgram;
import Spring.Visit.VisitModule.Dtos.VisitDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitProgramDTO {
    private Long id;
    private Long userId;
    private VisitDTO visitDTO;
    private DocumentDTO documentDTO;

    public static VisitProgramDTO toVisitProgramDTO(VisitProgram visitProgram){
        return new VisitProgramDTO(
                visitProgram.getId(),
                visitProgram.getAddedBy().getId(),
                visitProgram.getVisit() == null ? new VisitDTO() : VisitDTO.toVisitDTO(visitProgram.getVisit()),
                DocumentDTO.toDocumentDTO(visitProgram.getDocument())
        );
    }
}
