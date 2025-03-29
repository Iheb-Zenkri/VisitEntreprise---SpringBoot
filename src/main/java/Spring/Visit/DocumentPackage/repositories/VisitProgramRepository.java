package Spring.Visit.DocumentPackage.repositories;

import Spring.Visit.DocumentPackage.entities.VisitProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitProgramRepository extends JpaRepository<VisitProgram, Long> {
    Optional<VisitProgram> findByVisitId(Long visitId);
}
