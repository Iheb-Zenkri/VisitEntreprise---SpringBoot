package Spring.Visit.DocumentModule.repositories;

import Spring.Visit.DocumentModule.entities.VisitProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitProgramRepository extends JpaRepository<VisitProgram, Long> {
    Optional<VisitProgram> findByVisitId(Long visitId);
}
