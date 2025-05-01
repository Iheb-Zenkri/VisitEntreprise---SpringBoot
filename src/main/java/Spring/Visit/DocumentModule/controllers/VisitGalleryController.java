package Spring.Visit.DocumentModule.controllers;

import Spring.Visit.DocumentModule.dtos.VisitGalleryDTO;
import Spring.Visit.DocumentModule.services.DocumentService;
import Spring.Visit.DocumentModule.services.VisitGalleryService;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/visit-gallery")
public class VisitGalleryController {

    private final VisitGalleryService visitGalleryService;
    private final DocumentService documentService;

    public VisitGalleryController(VisitGalleryService visitGalleryService, DocumentService documentService) {
        this.visitGalleryService = visitGalleryService;
        this.documentService = documentService;
    }

    @PostMapping("/{visitId}")
    public ResponseEntity<VisitGalleryDTO> createVisitGallery(@PathVariable Long visitId) {
        VisitGalleryDTO visitGallery = visitGalleryService.newVisitGallery(visitId);
        return ResponseEntity.ok(visitGallery);
    }

    @PostMapping("/{visitGalleryId}/add-picture")
    public ResponseEntity<VisitGalleryDTO> addPictureToGallery(
            @PathVariable Long visitGalleryId,
            @RequestParam("file") MultipartFile file) throws IOException {
        VisitGalleryDTO updatedGallery = visitGalleryService.addPictureToGallery(file, visitGalleryId);
        return ResponseEntity.ok( updatedGallery);
    }

    @GetMapping("/{visitGalleryId}")
    public ResponseEntity<VisitGalleryDTO> getVisitGallery(@PathVariable Long visitGalleryId) {
        VisitGalleryDTO visitGallery = visitGalleryService.getGalleryById(visitGalleryId);
        return ResponseEntity.ok(visitGallery);
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<VisitGalleryDTO> getGalleryByVisitId(@PathVariable Long visitId) {
        VisitGalleryDTO visitGallery = visitGalleryService.getGalleryByVisitId(visitId);
        return ResponseEntity.ok(visitGallery);
    }

    @GetMapping("/picture/{documentId}")
    public ResponseEntity<FileSystemResource> getVisitGalleryPicture(@PathVariable Long documentId) {
        try {
            FileSystemResource fileResource = visitGalleryService.getVisitGalleryPicture(documentId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(fileResource.getFile().toPath())))
                    .body(fileResource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/{visitGalleryId}/remove-picture/{documentId}")
    public ResponseEntity<VisitGalleryDTO> removePictureFromGallery(
            @PathVariable Long visitGalleryId,
            @PathVariable Long documentId) {
        VisitGalleryDTO updatedGallery = visitGalleryService.removePictureFromGallery(visitGalleryId, documentId);
        return ResponseEntity.ok(updatedGallery);
    }

    @DeleteMapping("/{visitGalleryId}")
    public ResponseEntity<Void> deleteGallery(@PathVariable Long visitGalleryId) {
        visitGalleryService.deleteVisitGallery(visitGalleryId);
        return ResponseEntity.noContent().build();
    }

}
