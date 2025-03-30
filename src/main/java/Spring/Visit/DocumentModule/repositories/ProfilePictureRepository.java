package Spring.Visit.DocumentModule.repositories;

import Spring.Visit.DocumentModule.entities.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    Optional<ProfilePicture> findByUserId(Long userId);
}
