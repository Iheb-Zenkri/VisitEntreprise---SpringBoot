package Spring.Visit.repositories;

import Spring.Visit.entities.Unavailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UnavailabilityRepository extends JpaRepository<Unavailability, Long> {
    List<Unavailability> findByTeacherId(Long teacherId);
}

