package Spring.Visit.DocumentModule.dtos;

import Spring.Visit.DocumentModule.entities.CompanyPicture;
import Spring.Visit.DocumentModule.entities.ProfilePicture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyPictureDTO {
    private Long id ;
    private Long companyId ;
    private DocumentDTO documentDTO ;


    public static CompanyPictureDTO toCompanyPictureDTO(CompanyPicture companyPicture){
        return new CompanyPictureDTO(
                companyPicture.getId(),
                companyPicture.getCompany().getId(),
                DocumentDTO.toDocumentDTO(companyPicture.getDocument())
        );

    }
}
