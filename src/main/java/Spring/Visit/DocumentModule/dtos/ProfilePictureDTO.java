package Spring.Visit.DocumentModule.dtos;

import Spring.Visit.DocumentModule.entities.ProfilePicture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePictureDTO {
    private Long id ;
    private Long userId ;
    private DocumentDTO documentDTO ;


    public static ProfilePictureDTO toProfilePictureDTO(ProfilePicture profilePicture){
        return new ProfilePictureDTO(
                profilePicture.getId(),
                profilePicture.getUser().getId(),
                DocumentDTO.toDocumentDTO(profilePicture.getDocument())
        );

    }
}
