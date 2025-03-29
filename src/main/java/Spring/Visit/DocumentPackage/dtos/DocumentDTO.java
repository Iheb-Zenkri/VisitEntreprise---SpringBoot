package Spring.Visit.DocumentPackage.dtos;

import Spring.Visit.DocumentPackage.entities.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private Long id;
    private String title;
    private String uniqueName;
    private LocalDateTime uploadedAt;


    public static DocumentDTO toDocumentDTO(Document document){
        return new DocumentDTO(
                document.getId(),
                document.getTitle(),
                document.getUniqueName(),
                document.getUploadedAt()
        );
    }
}
