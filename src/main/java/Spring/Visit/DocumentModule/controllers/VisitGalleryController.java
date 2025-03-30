package Spring.Visit.DocumentModule.controllers;

import Spring.Visit.DocumentModule.dtos.VisitGalleryDTO;
import Spring.Visit.DocumentModule.services.VisitGalleryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/visit-gallery")
public class VisitGalleryController {

    private final VisitGalleryService visitGalleryService;

    public VisitGalleryController(VisitGalleryService visitGalleryService) {
        this.visitGalleryService = visitGalleryService;
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
