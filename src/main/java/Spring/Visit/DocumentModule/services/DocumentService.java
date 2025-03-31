package Spring.Visit.DocumentModule.services;

import Spring.Visit.DocumentModule.dtos.FileStoredDto;
import Spring.Visit.DocumentModule.entities.Document;
import Spring.Visit.DocumentModule.repositories.DocumentRepository;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final FileStorageService fileStorageService;
    private final DocumentRepository documentRepository;

    public DocumentService(FileStorageService fileStorageService, DocumentRepository documentRepository) {
        this.fileStorageService = fileStorageService;
        this.documentRepository = documentRepository;
    }

    public Document uploadDocument(MultipartFile file, String title) throws IOException {
        logger.info("Start uploading document with title: {}", title);

        FileStoredDto fileStoredDto = fileStorageService.storeFile(file);
        logger.info("File stored successfully with name: {}", fileStoredDto.getFileName());

        Document document = new Document();
        document.setTitle(title);
        document.setUniqueName(fileStoredDto.getFileName());
        document.setType(fileStoredDto.getMimeType());
        document.setFilePath(fileStoredDto.getFilePath().toString());

        Document savedDocument = documentRepository.save(document);
        logger.info("Document with ID: {} uploaded successfully", savedDocument.getId());

        return savedDocument;
    }

    public Document getDocument(Long documentId) {
        logger.info("Fetching document with ID: {}", documentId);

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> {
                    logger.error("Document with ID: {} not found", documentId);
                    return new ObjectNotFoundException("Document not found");
                });

        logger.info("Document with ID: {} found", documentId);
        return document;
    }

    public FileSystemResource getDocumentFile(String filePath) throws IOException {
        logger.info("Fetching file from path: {}", filePath);

        FileSystemResource file = fileStorageService.getFile(Path.of(filePath));
        logger.info("File retrieved successfully from path: {}", filePath);

        return file;
    }

    public boolean deleteDocument(Long documentId) throws IOException {
        logger.info("Deleting document with ID: {}", documentId);

        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> {
                    logger.error("Document with ID: {} not found", documentId);
                    return new ObjectNotFoundException("Document not found");
                });

        fileStorageService.deleteFile(doc.getUniqueName());
        documentRepository.delete(doc);

        logger.info("Document with ID: {} deleted successfully", documentId);
        return true;
    }
}
