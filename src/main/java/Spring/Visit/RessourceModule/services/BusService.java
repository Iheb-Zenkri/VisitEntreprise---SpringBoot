package Spring.Visit.RessourceModule.services;

import Spring.Visit.RessourceModule.dto.BusDTO;
import Spring.Visit.RessourceModule.entities.Bus;
import Spring.Visit.RessourceModule.enums.BusAvailability;
import Spring.Visit.RessourceModule.repositories.BusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BusService {
    private final BusRepository busRepository;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<Bus> GetBus() {
        return busRepository.findAll();
    }

    public void addBus(Bus bus) {
        Optional<Bus> busOptional = busRepository.findById(bus.getId());
        if (busOptional.isPresent()) {
            throw new IllegalStateException("Id exist! !");
        }
        busRepository.save(bus);
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
}
