package Spring.Visit.DocumentModule.services;

import Spring.Visit.DocumentModule.dtos.VisitGalleryDTO;
import Spring.Visit.DocumentModule.entities.Document;
import Spring.Visit.DocumentModule.entities.VisitGallery;
import Spring.Visit.DocumentModule.repositories.VisitGalleryRepository;
import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.SharedModule.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.repositories.UserRepository;
import Spring.Visit.VisitModule.entities.Visit;
import Spring.Visit.VisitModule.repositories.VisitRepository;
import Spring.Visit.VisitModule.services.VisitService;
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
import java.util.Optional;

@Service
public class VisitGalleryService {
    private static final Logger logger = LoggerFactory.getLogger(VisitGalleryService.class);

    private final VisitGalleryRepository visitGalleryRepository;
    private final DocumentService documentService;
    private final UserRepository userRepository;
    private final VisitRepository visitRepository;

    public VisitGalleryService(VisitGalleryRepository visitGalleryRepository, DocumentService documentService, UserRepository userRepository, VisitRepository visitRepository) {
        this.visitGalleryRepository = visitGalleryRepository;
        this.documentService = documentService;
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
    }

    public VisitGalleryDTO newVisitGallery(Long visitId) {
        logger.info("Creating new Visit Gallery for visitId: {}", visitId);

        Optional<VisitGallery> optionalVisitGallery = visitGalleryRepository.findByVisitId(visitId);
        if(optionalVisitGallery.isPresent()){
            return VisitGalleryDTO.toVisitGalleryDTO(optionalVisitGallery.get());
        }
        VisitGallery visitGallery = new VisitGallery();
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit not found"));
        visitGallery.setVisit(visit);
        visitGallery.setAddedBy(getAuthenticatedUser());

        VisitGalleryDTO visitGalleryDTO = VisitGalleryDTO.toVisitGalleryDTO(visitGalleryRepository.save(visitGallery));
        logger.info("Visit Gallery created successfully for visitId: {}", visitId);
        return visitGalleryDTO;
    }

    public VisitGalleryDTO getGalleryById(Long visitGalleryId) {
        logger.info("Fetching Visit Gallery with ID: {}", visitGalleryId);

        VisitGallery visitGallery = visitGalleryRepository.findById(visitGalleryId)
                .orElseThrow(() -> {
                    logger.error("Visit Gallery not found with ID: {}", visitGalleryId);
                    return new ObjectNotFoundException("Visit Gallery not found with ID " + visitGalleryId);
                });

        VisitGalleryDTO visitGalleryDTO = VisitGalleryDTO.toVisitGalleryDTO(visitGallery);
        logger.info("Visit Gallery fetched successfully with ID: {}", visitGalleryId);
        return visitGalleryDTO;
    }

    public VisitGalleryDTO getGalleryByVisitId(Long visitId) {
        VisitGallery visitGallery = visitGalleryRepository.findByVisitId(visitId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Gallery not found with ID " + visitId));

        return VisitGalleryDTO.toVisitGalleryDTO(visitGallery);
    }


    public FileSystemResource getVisitGalleryPicture(Long documentId) {
        try {
            return documentService.getDocumentFile(documentService.getDocument(documentId).getFilePath());
        } catch (IOException e) {
            throw new ObjectNotFoundException("Profile Picture not found");
        }
    }

    @Transactional
    public VisitGalleryDTO addPictureToGallery(MultipartFile file, Long visitGalleryId) throws IOException {
        logger.info("Adding picture to Visit Gallery with ID: {}", visitGalleryId);

        VisitGallery visitGallery = visitGalleryRepository.findById(visitGalleryId)
                .orElseThrow(() -> {
                    logger.error("Visit Gallery with ID {} not found", visitGalleryId);
                    return new ObjectNotFoundException("Visit Gallery with id " + visitGalleryId + " not Found.");
                });

        if (!Objects.equals(visitGallery.getAddedBy().getId(), getAuthenticatedUser().getId())) {
            logger.error("Unauthorized attempt to manage Visit Gallery added by another user. visitGalleryId: {}", visitGalleryId);
            throw new InvalidCredentialsException("You are not allowed to manage Visit Gallery added by another user.");
        }

        if (!file.getContentType().startsWith("image")) {
            logger.error("Invalid file type for adding picture. File type: {}", file.getContentType());
            throw new BadRequestException("Picture must be of type Image");
        }

        Document document = documentService.uploadDocument(file, "Visit Picture");
        visitGallery.addNewPictureToGallery(document);

        VisitGalleryDTO visitGalleryDTO = VisitGalleryDTO.toVisitGalleryDTO(visitGalleryRepository.save(visitGallery));
        logger.info("Picture added successfully to Visit Gallery with ID: {}", visitGalleryId);
        return visitGalleryDTO;
    }

    public VisitGalleryDTO removePictureFromGallery(Long visitGalleryId, Long documentId) {
        logger.info("Removing picture with documentId: {} from Visit Gallery with ID: {}", documentId, visitGalleryId);

        VisitGallery visitGallery = visitGalleryRepository.findById(visitGalleryId)
                .orElseThrow(() -> {
                    logger.error("Visit Gallery with ID {} not found", visitGalleryId);
                    return new ObjectNotFoundException("Visit Gallery with id " + visitGalleryId + " not Found.");
                });

        if (!Objects.equals(visitGallery.getAddedBy().getId(), getAuthenticatedUser().getId())) {
            logger.error("Unauthorized attempt to remove picture from Visit Gallery added by another user. visitGalleryId: {}, documentId: {}", visitGalleryId, documentId);
            throw new InvalidCredentialsException("You are not allowed to manage Visit Gallery added by another user.");
        }

        Document document = documentService.getDocument(documentId);
        visitGallery.removePictureFromGallery(document);

        try {
            documentService.deleteDocument(documentId);
            logger.info("Document with ID {} removed from gallery and deleted successfully", documentId);
        } catch (IOException e) {
            logger.error("Error deleting document with ID {} from gallery", documentId, e);
            throw new RuntimeException(e);
        }

        VisitGalleryDTO visitGalleryDTO = VisitGalleryDTO.toVisitGalleryDTO(visitGalleryRepository.save(visitGallery));
        logger.info("Picture removed successfully from Visit Gallery with ID: {}", visitGalleryId);
        return visitGalleryDTO;
    }

    @Transactional
    public void deleteVisitGallery(Long visitGalleryId) {
        logger.info("Deleting Visit Gallery with ID: {}", visitGalleryId);

        VisitGallery visitGallery = visitGalleryRepository.findById(visitGalleryId)
                .orElseThrow(() -> {
                    logger.error("Visit Gallery with ID {} not found", visitGalleryId);
                    return new ObjectNotFoundException("Visit Gallery not found with ID " + visitGalleryId);
                });

        if (!Objects.equals(visitGallery.getAddedBy().getId(), getAuthenticatedUser().getId())) {
            logger.error("Unauthorized attempt to delete Visit Gallery with ID: {}", visitGalleryId);
            throw new InvalidCredentialsException("You are not authorized to delete this gallery.");
        }

        for (Document document : visitGallery.getGallery()) {
            try {
                documentService.deleteDocument(document.getId());
                logger.info("Document with ID {} deleted successfully", document.getId());
            } catch (IOException e) {
                logger.error("Error deleting document with ID {} from Visit Gallery", document.getId(), e);
                throw new RuntimeException(e);
            }
        }

        visitGalleryRepository.delete(visitGallery);
        logger.info("Visit Gallery with ID {} deleted successfully", visitGalleryId);
    }

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> {
                        logger.error("User with email {} not found", username);
                        return new InvalidCredentialsException("User not found.");
                    });
        }
        logger.error("User is not authenticated");
        throw new InvalidCredentialsException("User is not authenticated as user.");
    }
}
