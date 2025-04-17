package Spring.Visit.VisitModule.services;

import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.repositories.UserRepository;
import Spring.Visit.VisitModule.Dtos.FeedBackCreationDTO;
import Spring.Visit.VisitModule.Dtos.FeedbackDTO;
import Spring.Visit.VisitModule.entities.Feedback;
import Spring.Visit.VisitModule.entities.Visit;
import Spring.Visit.VisitModule.repositories.FeedbackRepository;
import Spring.Visit.VisitModule.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final VisitRepository visitRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository, VisitRepository visitRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
    }

    public FeedbackDTO createFeedback(FeedBackCreationDTO feedBackCreationDTO) {
        User user = userRepository.findById(feedBackCreationDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with Id "+ feedBackCreationDTO.getUserId()+" not Found"));

        Visit visit = visitRepository.findById(feedBackCreationDTO.getVisitId())
                .orElseThrow(() -> new ObjectNotFoundException("Visit with Id "+ feedBackCreationDTO.getVisitId()+" not Found"));

        Feedback feedback = new Feedback();
        feedback.setAddedBy(user);
        feedback.setVisit(visit);
        feedback.setComment(feedBackCreationDTO.getComment());
        feedbackRepository.save(feedback);
        return FeedbackDTO.toFeedBackDTO(feedback);
    }

    public FeedbackDTO getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Feedback not found with id: " + id));
        return FeedbackDTO.toFeedBackDTO(feedback);
    }

    public List<FeedbackDTO> getAllFeedback() {
        return feedbackRepository.findAll().stream().map(FeedbackDTO::toFeedBackDTO).toList();
    }

    public FeedbackDTO updateFeedback(Long id, String comment) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Feedback not found with id: " + id));
        if(comment != null) feedback.setComment(comment);
        feedbackRepository.save(feedback);
        return FeedbackDTO.toFeedBackDTO(feedback);
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}