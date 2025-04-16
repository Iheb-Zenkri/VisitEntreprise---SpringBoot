package Spring.Visit.VisitModule.services;

import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.VisitModule.entities.Feedback;
import Spring.Visit.VisitModule.repositories.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Feedback not found with id: " + id));
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        Feedback feedback = getFeedbackById(id);
        if(updatedFeedback.getRating() != null) feedback.setRating(updatedFeedback.getRating());
        if(updatedFeedback.getComment() != null) feedback.setComment(updatedFeedback.getComment());
        if(updatedFeedback.getVisit() != null) feedback.setVisit(updatedFeedback.getVisit());
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}