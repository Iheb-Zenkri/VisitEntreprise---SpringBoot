package Spring.Visit.DocumentPackage.services;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

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
    public ProfilePicture uploadProfilePicture(MultipartFile file, Long userId) throws IOException {

        if(!file.getContentType().startsWith("image")){
            throw new BadRequestException("Profile Picture must be of type Image");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Document document = documentService.uploadDocument(file, "Profile Picture");


        ProfilePicture existingProfilePicture = profilePictureRepository.findByUserId(userId).orElse(null);

        if (existingProfilePicture != null) {
            documentService.deleteDocument(existingProfilePicture.getDocument().getId());
            profilePictureRepository.delete(existingProfilePicture);
        }

        ProfilePicture profilePicture = new ProfilePicture();
        profilePicture.setDocument(document);
        profilePicture.setUser(user);
        return profilePictureRepository.save(profilePicture);
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
        ProfilePicture profilePicture =profilePictureRepository.findByUserId(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Profile picture not found"));


        profilePictureRepository.delete(profilePicture);

        documentService.deleteDocument(profilePicture.getDocument().getId());

        return true;
    }
}

