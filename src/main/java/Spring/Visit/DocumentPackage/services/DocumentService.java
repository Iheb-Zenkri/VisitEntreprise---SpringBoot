package Spring.Visit.DocumentPackage.services;

import Spring.Visit.DocumentPackage.dtos.FileStoredDto;
import Spring.Visit.DocumentPackage.entities.Document;
import Spring.Visit.DocumentPackage.repositories.DocumentRepository;
import Spring.Visit.SharedPackage.exceptions.ObjectNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class DocumentService {
    private final FileStorageService fileStorageService;
    private final DocumentRepository documentRepository;

    public DocumentService(FileStorageService fileStorageService, DocumentRepository documentRepository) {
        this.fileStorageService = fileStorageService;
        this.documentRepository = documentRepository;
    }

    public Document uploadDocument(MultipartFile file, String title) throws IOException {

        FileStoredDto fileStoredDto = fileStorageService.storeFile(file);

        Document document = new Document();
        document.setTitle(title);
        document.setUniqueName(fileStoredDto.getFileName());
        document.setType(fileStoredDto.getMimeType());
        document.setFilePath(fileStoredDto.getFilePath().toString());

        return documentRepository.save(document);
    }

    public Document getDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ObjectNotFoundException("Document not found"));
    }

    public FileSystemResource getDocumentFile(String filePath) throws IOException {
        return fileStorageService.getFile(Path.of(filePath));
    }

    public boolean deleteDocument(Long documentId) throws IOException {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ObjectNotFoundException("Document not found"));

        fileStorageService.deleteFile(doc.getUniqueName());
        documentRepository.delete(doc);

        System.out.println("document with id : "+documentId+" deleted successfully");
        return true;
    }
}

