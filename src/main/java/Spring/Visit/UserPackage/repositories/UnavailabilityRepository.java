package Spring.Visit.UserPackage.repositories;

import Spring.Visit.UserPackage.entities.Unavailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UnavailabilityRepository extends JpaRepository<Unavailability, Long> {
    List<Unavailability> findByTeacherId(Long teacherId);
}

