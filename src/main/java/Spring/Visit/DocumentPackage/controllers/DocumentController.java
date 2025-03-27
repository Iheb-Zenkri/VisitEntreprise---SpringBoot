package Spring.Visit.DocumentPackage.controllers;

import Spring.Visit.DocumentPackage.entities.Document;
import Spring.Visit.DocumentPackage.services.DocumentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam("title") String title) {
        try {
            Document storedDocument = documentService.uploadDocument(file, title);
            return ResponseEntity.ok(storedDocument);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocument(id)) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        try {
            if (documentService.deleteDocument(id)) {
                return ResponseEntity.ok("File deleted successfully.");
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete file.");
        }
    }
}
