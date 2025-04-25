package Spring.Visit.RessourceModule.services;

import Spring.Visit.RessourceModule.entities.Agency;
import Spring.Visit.RessourceModule.repositories.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgencyService {

    private final AgencyRepository agencyRepository;

    @Autowired
    public AgencyService(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    public List<Agency> getAgencies() {
        return agencyRepository.findAll();
    }

    public void addAgency(Agency agency) {
        Optional<Agency> agencyOptional = agencyRepository.findByContactEmail(agency.getContactEmail());
        if (agencyOptional.isPresent()) {
            throw new IllegalStateException("Email déjà utilisé");
        }
        agencyRepository.save(agency);
    }

    public void deleteAgency(Long agencyId) {
        boolean exists = agencyRepository.existsById(agencyId);
        if (!exists) {
            throw new IllegalStateException("L'agence avec l'id " + agencyId + " n'existe pas.");
        }
        agencyRepository.deleteById(agencyId);
    }

    public void updateAgency(Long agencyId, String name, String address, String contactEmail, String contactPhone, Double rentFrequency) {
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new IllegalStateException("L'agence avec l'id " + agencyId + " n'existe pas."));

        if (name != null && !name.isEmpty() && !name.equals(agency.getName())) {
            agency.setName(name);
        }

        if (address != null && !address.isEmpty() && !address.equals(agency.getAddress())) {
            agency.setAddress(address);
        }

        if (contactEmail != null && !contactEmail.isEmpty() && !contactEmail.equals(agency.getContactEmail())) {
            Optional<Agency> agencyOptional = agencyRepository.findByContactEmail(contactEmail);
            if (agencyOptional.isPresent()) {
                throw new IllegalStateException("Email déjà utilisé.");
            }
            agency.setContactEmail(contactEmail);
        }

        if (contactPhone != null && !contactPhone.isEmpty() && !contactPhone.equals(agency.getContactPhone())) {
            agency.setContactPhone(contactPhone);
        }

        if (rentFrequency != null && rentFrequency != agency.getRentFrequency()) {
            agency.setRentFrequency(rentFrequency);
        }

        agencyRepository.save(agency);
    }
}
