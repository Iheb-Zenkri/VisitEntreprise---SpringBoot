package Spring.Visit.DocumentModule.dtos;

import Spring.Visit.DocumentModule.entities.VisitProgram;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitProgramDTO {
    private Long id;
    private Long userId;
    private Long visitId;
    private DocumentDTO documentDTO;

    public static VisitProgramDTO toVisitProgramDTO(VisitProgram visitProgram){
        return new VisitProgramDTO(
                visitProgram.getId(),
                visitProgram.getAddedBy().getId(),
                visitProgram.getVisitId(),
                DocumentDTO.toDocumentDTO(visitProgram.getDocument())
        );
    }
}
