package Spring.Visit.DocumentModule.repositories;

import Spring.Visit.DocumentModule.entities.CompanyPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyPictureRepository extends JpaRepository<CompanyPicture, Long> {
    Optional<CompanyPicture> findByCompanyId(Long companyId);
}
