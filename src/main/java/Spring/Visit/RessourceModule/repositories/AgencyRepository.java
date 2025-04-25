package Spring.Visit.RessourceModule.repositories;

import Spring.Visit.RessourceModule.entities.Agency;
import Spring.Visit.RessourceModule.entities.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Optional<Agency> findByContactEmail(String contactEmail);
}
