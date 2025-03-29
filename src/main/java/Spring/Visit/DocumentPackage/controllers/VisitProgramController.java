package Spring.Visit.DocumentPackage.controllers;

import Spring.Visit.DocumentPackage.entities.VisitProgram;
import Spring.Visit.DocumentPackage.services.VisitProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/visit-programs")
public class VisitProgramController {

    private final VisitProgramService visitProgramService;

    public VisitProgramController(VisitProgramService visitProgramService) {
        this.visitProgramService = visitProgramService;
    }

    @PostMapping("/{visitId}")
    public ResponseEntity<VisitProgram> createVisitProgram(@PathVariable Long visitId,
                                                           @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(visitProgramService.createVisitProgram(visitId, file));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitProgram> updateVisitProgramDocument(@PathVariable Long id,
                                                                   @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(visitProgramService.updateVisitProgramDocument(id, file));
    }


    @GetMapping("/{id}")
    public ResponseEntity<VisitProgram> getVisitProgram(@PathVariable Long id) {
        return ResponseEntity.ok(visitProgramService.getVisitProgram(id));
    }

    @GetMapping("/by-visit/{visitId}")
    public ResponseEntity<VisitProgram> getVisitProgramByVisitId(@PathVariable Long visitId) {
        return ResponseEntity.ok(visitProgramService.getVisitProgramByVisitId(visitId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVisitProgram(@PathVariable Long id) {
        visitProgramService.deleteVisitProgram(id);
        return ResponseEntity.ok("Visit Program deleted successfully.");
    }
}
