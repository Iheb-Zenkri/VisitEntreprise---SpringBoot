package Spring.Visit.DocumentPackage.services;

import Spring.Visit.DocumentPackage.dtos.VisitGalleryDTO;
import Spring.Visit.DocumentPackage.entities.Document;
import Spring.Visit.DocumentPackage.entities.VisitGallery;
import Spring.Visit.DocumentPackage.repositories.VisitGalleryRepository;
import Spring.Visit.SharedPackage.exceptions.BadRequestException;
import Spring.Visit.SharedPackage.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedPackage.exceptions.ObjectNotFoundException;
import Spring.Visit.UserPackage.entities.User;
import Spring.Visit.UserPackage.repositories.AdminRepository;
import Spring.Visit.UserPackage.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class VisitGalleryService {
    private final VisitGalleryRepository visitGalleryRepository;
    private final DocumentService documentService;
    private final UserRepository userRepository;

    public VisitGalleryService(VisitGalleryRepository visitGalleryRepository, DocumentService documentService, UserRepository userRepository) {
        this.visitGalleryRepository = visitGalleryRepository;
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    public VisitGalleryDTO newVisitGallery(Long visitId){
        VisitGallery visitGallery = new VisitGallery();
        visitGallery.setVisitId(visitId);
        visitGallery.setAddedBy(getAuthenticatedUser());
        return VisitGalleryDTO.toVisitGalleryDTO(visitGalleryRepository.save(visitGallery));
    }
    public VisitGalleryDTO getGalleryById(Long visitGalleryId) {
        VisitGallery visitGallery = visitGalleryRepository.findById(visitGalleryId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Gallery not found with ID " + visitGalleryId));
        return VisitGalleryDTO.toVisitGalleryDTO(visitGallery);
    }
    @Transactional
    public VisitGalleryDTO addPictureToGallery(MultipartFile file, Long visitGalleryId) throws IOException{
        VisitGallery visitGallery = visitGalleryRepository.findById(visitGalleryId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Gallery with id "+visitGalleryId+" not Found."));

        if(!Objects.equals(visitGallery.getAddedBy().getId(), getAuthenticatedUser().getId())){
            throw new InvalidCredentialsException("You are not allowed to manage Profile picture of another user.");
        }

        if(!file.getContentType().startsWith("image")){
            throw new BadRequestException("Profile Picture must be of type Image");
        }

        Document document = documentService.uploadDocument(file, "Visit Picture");

        visitGallery.addNewPictureToGallery(document);

        return VisitGalleryDTO.toVisitGalleryDTO(visitGalleryRepository.save(visitGallery));
    }
    public VisitGalleryDTO removePictureFromGallery(Long visitGalleryId,Long documentId){
        VisitGallery visitGallery = visitGalleryRepository.findById(visitGalleryId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Gallery with id "+visitGalleryId+" not Found."));

        if(!Objects.equals(visitGallery.getAddedBy().getId(), getAuthenticatedUser().getId())){
            throw new InvalidCredentialsException("You are not allowed to manage Visit Gallery added by another user.");
        }

        Document document = documentService.getDocument(documentId);
        visitGallery.removePictureFromGallery(document);

        try {
            documentService.deleteDocument(documentId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return VisitGalleryDTO.toVisitGalleryDTO(visitGalleryRepository.save(visitGallery));
    }
    @Transactional
    public void deleteVisitGallery(Long visitGalleryId) {
        VisitGallery visitGallery = visitGalleryRepository.findById(visitGalleryId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Gallery not found with ID " + visitGalleryId));

        if (!Objects.equals(visitGallery.getAddedBy().getId(), getAuthenticatedUser().getId())) {
            throw new InvalidCredentialsException("You are not authorized to delete this gallery.");
        }

        for (Document document : visitGallery.getGallery()) {
            try {
                documentService.deleteDocument(document.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        visitGalleryRepository.delete(visitGallery);
    }


    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new InvalidCredentialsException("User not found."));
        }
        throw new InvalidCredentialsException("User is not authenticated as user.");
    }
}
