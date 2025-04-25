package Spring.Visit.DocumentModule.services;

import Spring.Visit.DocumentModule.dtos.CompanyPictureDTO;
import Spring.Visit.DocumentModule.entities.CompanyPicture;
import Spring.Visit.DocumentModule.entities.Document;
import Spring.Visit.DocumentModule.repositories.CompanyPictureRepository;
import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.VisitModule.entities.Company;
import Spring.Visit.VisitModule.repositories.CompanyRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CompanyPictureService {

    private static final Logger logger = LoggerFactory.getLogger(ProfilePictureService.class);

    private final CompanyPictureRepository companyPictureRepository;
    private final DocumentService documentService;
    private final CompanyRepository companyRepository;

    public CompanyPictureService(CompanyPictureRepository companyPictureRepository, DocumentService documentService, CompanyRepository companyRepository) {
        this.companyPictureRepository = companyPictureRepository;
        this.documentService = documentService;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public CompanyPictureDTO uploadCompanyPicture(MultipartFile file, Long companyId) throws IOException {
        logger.info("Attempting to upload Company picture for companyId: {}", companyId);


        if (!file.getContentType().startsWith("image")) {
            logger.error("Invalid file type for company picture. File type: {}", file.getContentType());
            throw new BadRequestException("Company Picture must be of type Image");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found"));

        Document document = documentService.uploadDocument(file, "Company Picture");
        logger.info("Company picture document uploaded successfully for userId: {}", companyId);

        CompanyPicture existingCompanyPicture = companyPictureRepository.findByCompanyId(companyId).orElse(null);

        if (existingCompanyPicture != null) {
            logger.info("Replacing existing company picture for userId: {}", companyId);

            Long oldDocumentId = existingCompanyPicture.getDocument().getId();
            existingCompanyPicture.setDocument(document);
            companyPictureRepository.save(existingCompanyPicture);

            documentService.deleteDocument(oldDocumentId);
            logger.info("Old company picture deleted for userId: {}", companyId);
            return CompanyPictureDTO.toCompanyPictureDTO(existingCompanyPicture);
        } else {
            logger.info("Creating new profile picture for userId: {}", companyId);
            CompanyPicture companyPicture = new CompanyPicture();
            companyPicture.setDocument(document);
            companyPicture.setCompany(company);
            return CompanyPictureDTO.toCompanyPictureDTO(companyPictureRepository.save(companyPicture));
        }
    }

    public FileSystemResource getCompanyPicture(Long companyId) {
        logger.info("Fetching profile picture for userId: {}", companyId);

        CompanyPicture companyPicture = companyPictureRepository.findByCompanyId(companyId)
                .orElseThrow(() -> {
                    logger.error("Company picture not found for userId: {}", companyId);
                    return new ObjectNotFoundException("Company picture not found");
                });

        try {
            return documentService.getDocumentFile(companyPicture.getDocument().getFilePath());
        } catch (IOException e) {
            logger.error("Error fetching profile picture for userId: {}", companyId, e);
            throw new ObjectNotFoundException("Profile Picture not found");
        }
    }

    public void deleteCompanyPicture(Long companyId) throws IOException {
        logger.info("Attempting to delete company picture for userId: {}", companyId);

        CompanyPicture companyPicture = companyPictureRepository.findByCompanyId(companyId)
                .orElseThrow(() -> {
                    logger.error("Company picture not found for userId: {}", companyId);
                    return new ObjectNotFoundException("Company picture not found");
                });

        companyPictureRepository.delete(companyPicture);
        documentService.deleteDocument(companyPicture.getDocument().getId());

        logger.info("Company picture deleted successfully for userId: {}", companyPicture);
    }

}
