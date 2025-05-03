package Spring.Visit.VisitModule.controllers;

import Spring.Visit.VisitModule.Dtos.VisitCreationDTO;
import Spring.Visit.VisitModule.Dtos.VisitDTO;
import Spring.Visit.VisitModule.entities.Visit;
import Spring.Visit.VisitModule.services.VisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<VisitDTO> createVisit(@RequestBody VisitCreationDTO visitCreationDTO) {
        return ResponseEntity.ok(visitService.createVisit(visitCreationDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.getVisitById(id));
    }

    @GetMapping("/finished")
    public ResponseEntity<List<VisitDTO>> getAllVisits() {
        return ResponseEntity.ok(visitService.getFinishedVisits());
    }

    @GetMapping("/unfinished")
    public ResponseEntity<List<VisitDTO>> getUnfinishedVisits() {
        return ResponseEntity.ok(visitService.getUnfinishedVisits());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitDTO> updateVisit(@PathVariable Long id, @RequestBody VisitCreationDTO visit) {
        return ResponseEntity.ok(visitService.updateVisit(id, visit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteVisit(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.deleteVisit(id));
    }

    @PutMapping("/{visitId}/responsible/{teacherId}")
    public ResponseEntity<VisitDTO> addResponsibleToVisit(@PathVariable Long visitId, @PathVariable Long teacherId) {
        VisitDTO updatedVisit = visitService.addResponsibleToVisit(visitId, teacherId);
        return ResponseEntity.ok(updatedVisit);
    }

    @DeleteMapping("/{visitId}/responsible/{teacherId}")
    public ResponseEntity<VisitDTO> removeResponsibleFromVisit(@PathVariable Long visitId, @PathVariable Long teacherId) {
        VisitDTO updatedVisit = visitService.removeResponsibleFromVisit(visitId, teacherId);
        return ResponseEntity.ok(updatedVisit);
    }

    @PutMapping("/{visitId}/student-group/{groupId}")
    public ResponseEntity<VisitDTO> addStudentGroupToVisit(@PathVariable Long visitId, @PathVariable Long groupId) {
        VisitDTO updatedVisit = visitService.addStudentGroupToVisit(visitId, groupId);
        return ResponseEntity.ok(updatedVisit);
    }

    @DeleteMapping("/{visitId}/student-group/{groupId}")
    public ResponseEntity<VisitDTO> removeStudentGroupFromVisit(@PathVariable Long visitId, @PathVariable Long groupId) {
        VisitDTO updatedVisit = visitService.removeStudentGroupFromVisit(visitId, groupId);
        return ResponseEntity.ok(updatedVisit);
    }
}