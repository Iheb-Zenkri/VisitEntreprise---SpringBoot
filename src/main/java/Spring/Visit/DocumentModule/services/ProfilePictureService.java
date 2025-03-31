package Spring.Visit.DocumentModule.services;

import Spring.Visit.DocumentModule.dtos.ProfilePictureDTO;
import Spring.Visit.DocumentModule.entities.Document;
import Spring.Visit.DocumentModule.entities.ProfilePicture;
import Spring.Visit.DocumentModule.repositories.ProfilePictureRepository;
import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.SharedModule.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ProfilePictureService {
    private static final Logger logger = LoggerFactory.getLogger(ProfilePictureService.class);

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
        logger.info("Attempting to upload profile picture for userId: {}", userId);

        if (!Objects.equals(userId, getAuthenticatedUserId())) {
            logger.error("Unauthorized attempt to manage profile picture of user: {}", userId);
            throw new InvalidCredentialsException("You are not allowed to manage Profile picture of another user.");
        }

        if (!file.getContentType().startsWith("image")) {
            logger.error("Invalid file type for profile picture. File type: {}", file.getContentType());
            throw new BadRequestException("Profile Picture must be of type Image");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with userId: {}", userId);
                    return new UserNotFoundException("User not found");
                });

        Document document = documentService.uploadDocument(file, "Profile Picture");
        logger.info("Profile picture document uploaded successfully for userId: {}", userId);

        ProfilePicture existingProfilePicture = profilePictureRepository.findByUserId(userId).orElse(null);

        if (existingProfilePicture != null) {
            logger.info("Replacing existing profile picture for userId: {}", userId);

            Long oldDocumentId = existingProfilePicture.getDocument().getId();
            existingProfilePicture.setDocument(document);
            profilePictureRepository.save(existingProfilePicture);

            documentService.deleteDocument(oldDocumentId);
            logger.info("Old profile picture deleted for userId: {}", userId);
            return ProfilePictureDTO.toProfilePictureDTO(existingProfilePicture);
        } else {
            logger.info("Creating new profile picture for userId: {}", userId);
            ProfilePicture profilePicture = new ProfilePicture();
            profilePicture.setDocument(document);
            profilePicture.setUser(user);
            return ProfilePictureDTO.toProfilePictureDTO(profilePictureRepository.save(profilePicture));
        }
    }

    public FileSystemResource getProfilePicture(Long userId) {
        logger.info("Fetching profile picture for userId: {}", userId);

        ProfilePicture profilePicture = profilePictureRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Profile picture not found for userId: {}", userId);
                    return new ObjectNotFoundException("Profile picture not found");
                });

        try {
            return documentService.getDocumentFile(profilePicture.getDocument().getFilePath());
        } catch (IOException e) {
            logger.error("Error fetching profile picture for userId: {}", userId, e);
            throw new ObjectNotFoundException("Profile Picture not found");
        }
    }

    public boolean deleteProfilePicture(Long userId) throws IOException {
        logger.info("Attempting to delete profile picture for userId: {}", userId);

        if (!Objects.equals(userId, getAuthenticatedUserId())) {
            logger.error("Unauthorized attempt to delete profile picture of user: {}", userId);
            throw new InvalidCredentialsException("You are not allowed to manage Profile picture of another user.");
        }

        ProfilePicture profilePicture = profilePictureRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Profile picture not found for userId: {}", userId);
                    return new ObjectNotFoundException("Profile picture not found");
                });

        profilePictureRepository.delete(profilePicture);
        documentService.deleteDocument(profilePicture.getDocument().getId());

        logger.info("Profile picture deleted successfully for userId: {}", userId);
        return true;
    }

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> {
                        logger.error("User not found with email: {}", username);
                        return new UserNotFoundException("Teacher not found");
                    });
            return user.getId();
        }
        logger.error("User is not authenticated");
        throw new InvalidCredentialsException("User is not authenticated");
    }
}
