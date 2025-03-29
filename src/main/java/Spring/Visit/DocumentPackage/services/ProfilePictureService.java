package Spring.Visit.DocumentPackage.services;

import Spring.Visit.DocumentPackage.dtos.ProfilePictureDTO;
import Spring.Visit.DocumentPackage.entities.Document;
import Spring.Visit.DocumentPackage.entities.ProfilePicture;
import Spring.Visit.DocumentPackage.repositories.ProfilePictureRepository;
import Spring.Visit.SharedPackage.exceptions.BadRequestException;
import Spring.Visit.SharedPackage.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedPackage.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedPackage.exceptions.UserNotFoundException;
import Spring.Visit.UserPackage.entities.User;
import Spring.Visit.UserPackage.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Objects;

@Service
public class ProfilePictureService {
    private final ProfilePictureRepository profilePictureRepository;
    private final DocumentService documentService;
    private final UserRepository userRepository;

    public ProfilePictureService(ProfilePictureRepository profilePictureRepository, DocumentService documentService, UserRepository userRepository) {
        this.profilePictureRepository = profilePictureRepository;
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProfilePictureDTO uploadProfilePicture(MultipartFile file, Long userId) throws IOException {

        if(!Objects.equals(userId, getAuthenticatedUserId())){
            throw new InvalidCredentialsException("You are not allowed to manage Profile picture of another user.");
        }

        if(!file.getContentType().startsWith("image")){
            throw new BadRequestException("Profile Picture must be of type Image");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Document document = documentService.uploadDocument(file, "Profile Picture");


        ProfilePicture existingProfilePicture = profilePictureRepository.findByUserId(userId).orElse(null);

        if (existingProfilePicture != null) {
            //// in case of replacing the old Picture with new one
            //// extract the old document id (old picture file)
            Long oldDocumentId = existingProfilePicture.getDocument().getId();

            //// replace the old document by the new document in the exciting profile picture and save it on DB
            existingProfilePicture.setDocument(document);
            profilePictureRepository.save(existingProfilePicture);

            //// delete the old document and return the new profile picture
            documentService.deleteDocument(oldDocumentId);
            return ProfilePictureDTO.toProfilePictureDTO(existingProfilePicture);
        }else{
            //// in case of a new Profile picture
            ProfilePicture profilePicture = new ProfilePicture();
            profilePicture.setDocument(document);
            profilePicture.setUser(user);
            return ProfilePictureDTO.toProfilePictureDTO(profilePictureRepository.save(profilePicture));
        }
    }

    public FileSystemResource getProfilePicture(Long userId) {
        ProfilePicture profilePicture =profilePictureRepository.findByUserId(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Profile picture not found"));

        try {
            return documentService.getDocumentFile(profilePicture.getDocument().getFilePath());
        } catch (IOException e) {
            throw new ObjectNotFoundException("Profile Picture not found");
        }
    }

    public boolean deleteProfilePicture(Long userId) throws IOException {

        if(!Objects.equals(userId, getAuthenticatedUserId())){
            throw new InvalidCredentialsException("You are not allowed to manage Profile picture of another user.");
        }

        ProfilePicture profilePicture =profilePictureRepository.findByUserId(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Profile picture not found"));


        profilePictureRepository.delete(profilePicture);

        documentService.deleteDocument(profilePicture.getDocument().getId());

        return true;
    }

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return (long) userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("Teacher not found"))
                    .getId();
        }
        throw new InvalidCredentialsException("User is not authenticated");
    }
}

