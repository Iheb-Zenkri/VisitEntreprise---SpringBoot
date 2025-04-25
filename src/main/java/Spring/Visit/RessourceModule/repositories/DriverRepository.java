package Spring.Visit.RessourceModule.repositories;

import Spring.Visit.RessourceModule.entities.Bus;
import Spring.Visit.RessourceModule.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByLicenseNumber(Long licenseNumber);
    Optional<Driver> findByContactPhone(Long contactPhone);
}
