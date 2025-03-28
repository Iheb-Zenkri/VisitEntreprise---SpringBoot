package Spring.Visit.DocumentPackage.controllers;

import Spring.Visit.DocumentPackage.entities.VisitGallery;
import Spring.Visit.DocumentPackage.services.VisitGalleryService;
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

    @PostMapping("/new/{visitId}")
    public ResponseEntity<VisitGallery> createVisitGallery(@PathVariable Long visitId) {
        VisitGallery visitGallery = visitGalleryService.newVisitGallery(visitId);
        return ResponseEntity.ok(visitGallery);
    }

    @PostMapping("/{visitGalleryId}/add-picture")
    public ResponseEntity<VisitGallery> addPictureToGallery(
            @PathVariable Long visitGalleryId,
            @RequestParam("file") MultipartFile file) throws IOException {
        VisitGallery updatedGallery = visitGalleryService.addPictureToGallery(file, visitGalleryId);
        return ResponseEntity.ok( updatedGallery);
    }

    @DeleteMapping("/{visitGalleryId}/remove-picture/{documentId}")
    public ResponseEntity<VisitGallery> removePictureFromGallery(
            @PathVariable Long visitGalleryId,
            @PathVariable Long documentId) {
        VisitGallery updatedGallery = visitGalleryService.removePictureFromGallery(visitGalleryId, documentId);
        return ResponseEntity.ok(updatedGallery);
    }

    @DeleteMapping("/{visitGalleryId}")
    public ResponseEntity<Void> deleteGallery(@PathVariable Long visitGalleryId) {
        visitGalleryService.deleteVisitGallery(visitGalleryId);
        return ResponseEntity.noContent().build();
    }

}
