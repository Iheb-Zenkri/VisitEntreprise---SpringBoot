package Spring.Visit.RessourceModule.services;

import Spring.Visit.RessourceModule.dto.DriverDTO;
import Spring.Visit.RessourceModule.entities.Driver;
import Spring.Visit.RessourceModule.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> getDrivers() {
        return driverRepository.findAll();
    }

    public void addDriver(DriverDTO driverDTO) {
        Optional<Driver> driverOptional = driverRepository.findByLicenseNumber(driverDTO.getLicenseNumber());
        if (driverOptional.isPresent()) {
            throw new IllegalStateException("Numéro de permis déjà utilisé.");
        }

        Optional<Driver> phoneOptional = driverRepository.findByContactPhone(driverDTO.getContactPhone());
        if (phoneOptional.isPresent()) {
            throw new IllegalStateException("Numéro de téléphone déjà utilisé.");
        }
        Driver driver = new Driver(
                driverDTO.getName(),
                driverDTO.getLicenseNumber(),
                driverDTO.getContactPhone()
        );
        driverRepository.save(driver);

    }

    public void deleteDriver(Long driverId) {
        boolean exists = driverRepository.existsById(driverId);
        if (!exists) {
            throw new IllegalStateException("Le conducteur avec l'ID " + driverId + " n'existe pas.");
        }
        driverRepository.deleteById(driverId);
    }

    public void updateDriver(Long driverId, String name, Long licenseNumber, Long contactPhone) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalStateException("Le conducteur avec l'ID " + driverId + " n'existe pas."));

        if (name != null && !name.isEmpty() && !name.equals(driver.getName())) {
            driver.setName(name);
        }

        if (licenseNumber != null && !licenseNumber.equals(driver.getLicenseNumber())) {
            Optional<Driver> licenseOptional = driverRepository.findByLicenseNumber(licenseNumber);
            if (licenseOptional.isPresent()) {
                throw new IllegalStateException("Numéro de permis déjà utilisé.");
            }
            driver.setLicenseNumber(licenseNumber);
        }

        if (contactPhone != null && !contactPhone.equals(driver.getContactPhone())) {
            Optional<Driver> phoneOptional = driverRepository.findByContactPhone(contactPhone);
            if (phoneOptional.isPresent()) {
                throw new IllegalStateException("Numéro de téléphone déjà utilisé.");
            }
            driver.setContactPhone(contactPhone);
        }

        driverRepository.save(driver);
    }
}
