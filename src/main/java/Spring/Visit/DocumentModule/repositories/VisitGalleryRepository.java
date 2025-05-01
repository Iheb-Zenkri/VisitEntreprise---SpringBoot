package Spring.Visit.DocumentModule.repositories;

import Spring.Visit.DocumentModule.entities.VisitGallery;
import Spring.Visit.DocumentModule.entities.VisitProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitGalleryRepository extends JpaRepository<VisitGallery, Long> {
    Optional<VisitGallery> findByVisitId(Long visitId);

}
