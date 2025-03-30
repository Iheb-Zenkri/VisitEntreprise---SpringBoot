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
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VisitProgramService {

    private final VisitProgramRepository visitProgramRepository;
    private final DocumentService documentService;
    private final UserRepository userRepository;

    public VisitProgramService(VisitProgramRepository visitProgramRepository,
                               DocumentService documentService,
                               UserRepository userRepository) {
        this.visitProgramRepository = visitProgramRepository;
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @Transactional
    public VisitProgramDTO createVisitProgram(Long visitId, MultipartFile file) throws IOException {
        User authenticatedUser = getAuthenticatedUser();

        String contentType = file.getContentType();
        if (notIsAllowedFileType(contentType)) {
            throw new BadRequestException("this document types is not allowed.");
        }

        Document document = documentService.uploadDocument(file, file.getOriginalFilename());

        VisitProgram visitProgram = new VisitProgram();
        visitProgram.setVisitId(visitId);
        visitProgram.setAddedBy(authenticatedUser);
        visitProgram.setDocument(document);
        return VisitProgramDTO.toVisitProgramDTO(visitProgramRepository.save(visitProgram));
    }

    @Transactional
    public VisitProgramDTO updateVisitProgramDocument(Long visitProgramId, MultipartFile file) throws IOException {
        VisitProgram visitProgram = visitProgramRepository.findById(visitProgramId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Program not found"));

        if (!Objects.equals(visitProgram.getAddedBy().getId(), getAuthenticatedUser().getId())) {
            throw new InvalidCredentialsException("You are not allowed to update this Visit Program.");
        }

        String contentType = file.getContentType();
        if (notIsAllowedFileType(contentType)) {
            throw new BadRequestException("this document types is not allowed.");
        }

        String newTitle = generateNextDocumentTitle(visitProgram.getDocument().getTitle());

        Document newDocument = documentService.uploadDocument(file, newTitle);

        documentService.deleteDocument(visitProgram.getDocument().getId());
        visitProgram.setDocument(newDocument);

        return VisitProgramDTO.toVisitProgramDTO(visitProgramRepository.save(visitProgram));
    }

    public VisitProgramDTO getVisitProgram(Long visitProgramId) {
        VisitProgram visitProgram = visitProgramRepository.findById(visitProgramId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Program not found"));
        return VisitProgramDTO.toVisitProgramDTO(visitProgram);
    }

    public VisitProgramDTO getVisitProgramByVisitId(Long visitId) {
        VisitProgram visitProgram =  visitProgramRepository.findByVisitId(visitId)
                .orElseThrow(() -> new ObjectNotFoundException("No Visit Program found for visitId: " + visitId));
        return VisitProgramDTO.toVisitProgramDTO(visitProgram);
    }

    @Transactional
    public void deleteVisitProgram(Long visitProgramId) {
        VisitProgram visitProgram = visitProgramRepository.findById(visitProgramId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit Program not found"));

        if (!Objects.equals(visitProgram.getAddedBy().getId(), getAuthenticatedUser().getId())) {
            throw new InvalidCredentialsException("You are not allowed to delete this Visit Program.");
        }

        try {
            visitProgramRepository.delete(visitProgram);
            documentService.deleteDocument(visitProgram.getDocument().getId());
        } catch (IOException e) {
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
