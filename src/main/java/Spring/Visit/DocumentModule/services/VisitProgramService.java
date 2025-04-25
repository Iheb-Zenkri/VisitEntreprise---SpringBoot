package Spring.Visit.DocumentModule.services;

import Spring.Visit.DocumentModule.dtos.VisitProgramDTO;
import Spring.Visit.DocumentModule.entities.Document;
import Spring.Visit.DocumentModule.entities.VisitProgram;
import Spring.Visit.DocumentModule.repositories.VisitProgramRepository;
import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.SharedModule.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.repositories.UserRepository;
import Spring.Visit.VisitModule.entities.Visit;
import Spring.Visit.VisitModule.repositories.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VisitProgramService {

    private static final Logger logger = LoggerFactory.getLogger(VisitProgramService.class);

    private final VisitProgramRepository visitProgramRepository;
    private final DocumentService documentService;
    private final UserRepository userRepository;
    private final VisitRepository visitRepository ;

    public VisitProgramService(VisitProgramRepository visitProgramRepository,
                               DocumentService documentService,
                               UserRepository userRepository, VisitRepository visitRepository) {
        this.visitProgramRepository = visitProgramRepository;
        this.documentService = documentService;
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
    }

    @Transactional
    public VisitProgramDTO createVisitProgram(Long visitId, MultipartFile file) throws IOException {
        User authenticatedUser = getAuthenticatedUser();
        logger.info("Creating visit program for visit ID: {}", visitId);

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit not found"));

        String contentType = file.getContentType();
        if (notIsAllowedFileType(contentType)) {
            logger.warn("File type not allowed: {}", contentType);
            throw new BadRequestException("This document type is not allowed.");
        }

        Document document = documentService.uploadDocument(file, file.getOriginalFilename());
        logger.info("Document uploaded successfully: {}", document.getTitle());

        VisitProgram visitProgram = new VisitProgram();
        visitProgram.setVisit(visit);
        visitProgram.setAddedBy(authenticatedUser);
        visitProgram.setDocument(document);

        VisitProgram savedVisitProgram = visitProgramRepository.save(visitProgram);
        logger.info("Visit program created successfully with ID: {}", savedVisitProgram.getId());

        return VisitProgramDTO.toVisitProgramDTO(savedVisitProgram);
    }

    @Transactional
    public VisitProgramDTO updateVisitProgramDocument(Long visitProgramId, MultipartFile file) throws IOException {
        logger.info("Updating visit program document with ID: {}", visitProgramId);

        VisitProgram visitProgram = visitProgramRepository.findById(visitProgramId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Program not found"));

        if (!Objects.equals(visitProgram.getAddedBy().getId(), getAuthenticatedUser().getId())) {
            logger.warn("Unauthorized attempt to update visit program ID: {}", visitProgramId);
            throw new InvalidCredentialsException("You are not allowed to update this Visit Program.");
        }

        String contentType = file.getContentType();
        if (notIsAllowedFileType(contentType)) {
            logger.warn("File type not allowed: {}", contentType);
            throw new BadRequestException("This document type is not allowed.");
        }

        String newTitle = generateNextDocumentTitle(visitProgram.getDocument().getTitle());
        Document newDocument = documentService.uploadDocument(file, newTitle);
        documentService.deleteDocument(visitProgram.getDocument().getId());

        visitProgram.setDocument(newDocument);
        VisitProgram savedVisitProgram = visitProgramRepository.save(visitProgram);
        logger.info("Visit program document updated successfully for ID: {}", savedVisitProgram.getId());

        return VisitProgramDTO.toVisitProgramDTO(savedVisitProgram);
    }

    public VisitProgramDTO getVisitProgram(Long visitProgramId) {
        logger.info("Fetching visit program with ID: {}", visitProgramId);

        VisitProgram visitProgram = visitProgramRepository.findById(visitProgramId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Program not found"));

        return VisitProgramDTO.toVisitProgramDTO(visitProgram);
    }

    public VisitProgramDTO getVisitProgramByVisitId(Long visitId) {
        logger.info("Fetching visit program by visitId: {}", visitId);

        VisitProgram visitProgram =  visitProgramRepository.findByVisitId(visitId)
                .orElseThrow(() -> new ObjectNotFoundException("No Visit Program found for visitId: " + visitId));
        return VisitProgramDTO.toVisitProgramDTO(visitProgram);
    }

    @Transactional
    public void deleteVisitProgram(Long visitProgramId) {
        logger.info("Deleting visit program with ID: {}", visitProgramId);

        VisitProgram visitProgram = visitProgramRepository.findById(visitProgramId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Program not found"));

        if (!Objects.equals(visitProgram.getAddedBy().getId(), getAuthenticatedUser().getId())) {
            logger.warn("Unauthorized attempt to delete visit program ID: {}", visitProgramId);
            throw new InvalidCredentialsException("You are not allowed to delete this Visit Program.");
        }

        try {
            visitProgramRepository.delete(visitProgram);
            documentService.deleteDocument(visitProgram.getDocument().getId());
            logger.info("Visit program with ID {} and its document deleted successfully.", visitProgramId);
        } catch (IOException e) {
            logger.error("Error while deleting visit program with ID {}: {}", visitProgramId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Helper methods
    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new InvalidCredentialsException("User not found."));
        }
        throw new InvalidCredentialsException("User is not authenticated.");
    }


    private boolean notIsAllowedFileType(String contentType) {
        return contentType == null || (!contentType.equals("application/pdf") && // PDF documents
                !contentType.equals("application/msword") && // DOC (older Word format)
                !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") && // DOCX (new Word format)
                !contentType.equals("application/vnd.ms-excel") && // XLS (older Excel format)
                !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") && // XLSX (new Excel format)
                !contentType.equals("application/vnd.ms-powerpoint") && // PPT (older PowerPoint format)
                !contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation") && // PPTX (new PowerPoint format)
                !contentType.equals("text/plain") && // TXT (plain text files)
                !contentType.equals("text/csv"));
    }

    /**
     * Generates the next document title by incrementing the suffix number.
     * Example: "doc" -> "doc (1)", "file (3)" -> "file (4)"
     */
    private String generateNextDocumentTitle(String oldTitle) {
        if (oldTitle == null || oldTitle.isEmpty()) {
            return "Document (1)";
        }

        // Regex to find "(number)" at the end of the title
        Pattern pattern = Pattern.compile("^(.*) \\((\\d+)\\)$");
        Matcher matcher = pattern.matcher(oldTitle);

        if (matcher.matches()) {
            String baseTitle = matcher.group(1);
            int version = Integer.parseInt(matcher.group(2)) + 1;
            return baseTitle + " (" + version + ")";
        } else {
            return oldTitle + " (1)";
        }
    }

}
