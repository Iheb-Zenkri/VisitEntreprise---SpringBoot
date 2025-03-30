package Spring.Visit.UserModule.repositories;

import Spring.Visit.UserModule.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);
}

