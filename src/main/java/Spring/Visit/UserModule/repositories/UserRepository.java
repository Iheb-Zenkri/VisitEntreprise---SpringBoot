package Spring.Visit.UserModule.repositories;

import Spring.Visit.UserModule.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Spring.Visit.UserModule.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<User> findByRole(UserRole role, Pageable pageable);

}
