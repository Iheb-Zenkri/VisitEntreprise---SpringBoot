package Spring.Visit.DocumentModule.dtos;

import Spring.Visit.DocumentModule.entities.VisitGallery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitGalleryDTO {
    private Long id ;
    private Long userId;
    private Long visitId;
    private List<DocumentDTO> documentDTO;

    public static VisitGalleryDTO toVisitGalleryDTO(VisitGallery visitGallery){
        return new VisitGalleryDTO(
            visitGallery.getId(),
            visitGallery.getAddedBy().getId(),
            visitGallery.getVisit().getId(),
            visitGallery.getGallery() != null ? visitGallery.getGallery().stream().map(DocumentDTO::toDocumentDTO).toList() : Collections.emptyList()
        );
    }
}
