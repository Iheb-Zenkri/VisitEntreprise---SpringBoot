package Spring.Visit.DocumentPackage.dtos;

import Spring.Visit.DocumentPackage.entities.ProfilePicture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
