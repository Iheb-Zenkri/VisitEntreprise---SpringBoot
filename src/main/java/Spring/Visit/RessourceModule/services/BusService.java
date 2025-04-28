package Spring.Visit.RessourceModule.services;

import Spring.Visit.RessourceModule.dto.BusDTO;
import Spring.Visit.RessourceModule.entities.Agency;
import Spring.Visit.RessourceModule.entities.Bus;
import Spring.Visit.RessourceModule.entities.Driver;
import Spring.Visit.RessourceModule.enums.BusAvailability;
import Spring.Visit.RessourceModule.repositories.AgencyRepository;
import Spring.Visit.RessourceModule.repositories.BusRepository;
import Spring.Visit.RessourceModule.repositories.DriverRepository;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BusService {
    private final BusRepository busRepository;
    private final AgencyRepository agencyRepository;
    private final DriverRepository driverRepository;
    @Autowired
    public BusService(BusRepository busRepository, AgencyRepository agencyRepository, DriverRepository driverRepository) {
        this.busRepository = busRepository;
        this.agencyRepository = agencyRepository;
        this.driverRepository = driverRepository;
    }

    public List<BusDTO> GetBus() {
        return busRepository.findAll().stream().map(BusDTO::toBusDTO).toList();
    }

    public BusDTO addBus(Bus bus) {
        return BusDTO.toBusDTO(busRepository.save(bus));
    }
/*

public void addBus(BusDTO busDTO) {
    Optional<Bus> existing = busRepository.findByRegistrationNumber(busDTO.getRegistrationNumber());
    if (existing.isPresent()) {
        throw new IllegalStateException("Ce numéro d'immatriculation est déjà utilisé.");
    }

    Agency agency = agencyRepository.findById(busDTO.getAgencyId())
            .orElseThrow(() -> new IllegalStateException("Agence non trouvée avec l'ID : " + busDTO.getAgencyId()));

    Bus bus = BusMapper.toEntity(busDTO, agency);
    busRepository.save(bus);
}*/
    public void deleteBus(Long busId){

        boolean exists = busRepository.existsById(busId);
         if(!exists){
             throw new IllegalStateException("Bus with id "+ busId + " does not exists");
         }
         busRepository.deleteById(busId);

    }

    @Transactional
    public void updateBus(Long busId, BusAvailability availability) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new IllegalStateException("Bus with id " + busId + " does not exist"));

        if (availability != null && !Objects.equals(bus.getAvailability(), availability)) {
            bus.setAvailability(availability);
        }
    }

    public BusDTO addBusToAgency(Long busId,Long agencyId){
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ObjectNotFoundException("Bus not found"));
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Agency not found"));

        if(!agency.getBuses().contains(bus)){
            agency.getBuses().add(bus);
            agencyRepository.save(agency);
        }

        bus.setAgency(agency);
        return BusDTO.toBusDTO(busRepository.save(bus));
    }

    public BusDTO addDriverToBus(Long busId,Long driverId){
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ObjectNotFoundException("Bus not found"));
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ObjectNotFoundException("Agency not found"));

        driver.setBus(bus);
        driverRepository.save(driver);

        bus.setDriver(driver);
        return BusDTO.toBusDTO(busRepository.save(bus));
    }
}
