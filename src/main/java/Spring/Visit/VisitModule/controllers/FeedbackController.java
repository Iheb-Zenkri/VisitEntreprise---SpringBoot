package Spring.Visit.VisitModule.controllers;

import Spring.Visit.VisitModule.Dtos.FeedBackCreationDTO;
import Spring.Visit.VisitModule.Dtos.FeedbackDTO;
import Spring.Visit.VisitModule.entities.Feedback;
import Spring.Visit.VisitModule.services.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedBackCreationDTO feedback) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedback));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacksByVisit(@PathVariable Long visitId) {
        List<FeedbackDTO> feedbacks = feedbackService.getAllFeedbackByVisit(visitId);
        return ResponseEntity.ok(feedbacks);
    }
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDTO> updateFeedback(@PathVariable Long id, @RequestBody String comment) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        Map<String,String> response = new HashMap<>();
        response.put("message","Commentaire supprimé avec succès.");
        return ResponseEntity.ok(response);
    }
}