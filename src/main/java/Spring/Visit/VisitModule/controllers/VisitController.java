package Spring.Visit.VisitModule.controllers;

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
    public ResponseEntity<Visit> createVisit(@RequestBody Visit visit) {
        return ResponseEntity.ok(visitService.createVisit(visit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visit> getVisitById(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.getVisitById(id));
    }

    @GetMapping
    public ResponseEntity<List<Visit>> getAllVisits() {
        return ResponseEntity.ok(visitService.getAllVisits());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visit> updateVisit(@PathVariable Long id, @RequestBody Visit visit) {
        return ResponseEntity.ok(visitService.updateVisit(id, visit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteVisit(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.deleteVisit(id));
    }
}