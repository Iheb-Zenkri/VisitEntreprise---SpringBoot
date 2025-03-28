package Spring.Visit.DocumentPackage.repositories;

import Spring.Visit.DocumentPackage.entities.VisitGallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitGalleryRepository extends JpaRepository<VisitGallery, Long> {
}
