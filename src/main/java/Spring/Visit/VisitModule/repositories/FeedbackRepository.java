package Spring.Visit.VisitModule.repositories;

import Spring.Visit.VisitModule.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByVisitId(Long visitId);
}