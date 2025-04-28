package Spring.Visit.RessourceModule.controllers;

import Spring.Visit.RessourceModule.dto.AgencyDTO;
import Spring.Visit.RessourceModule.entities.Agency;
import Spring.Visit.RessourceModule.services.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/agency")
public class AgencyController {

    private final AgencyService agencyService;

    @Autowired
    public AgencyController(AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @GetMapping
    public List<AgencyDTO> getAgencies() {
        return agencyService.getAgencies();
    }

    @PostMapping
    public AgencyDTO addAgency(@RequestBody Agency agency) {
        return agencyService.addAgency(agency);
    }

    @DeleteMapping(path = "{agencyId}")
    public void deleteAgency(@PathVariable("agencyId") Long agencyId) {
        agencyService.deleteAgency(agencyId);
    }

    @PutMapping(path = "{agencyId}")
    public void updateAgency(
            @PathVariable("agencyId") Long agencyId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String contactEmail,
            @RequestParam(required = false) String contactPhone,
            @RequestParam(required = false) Double rentFrequency) {
        agencyService.updateAgency(agencyId, name, address, contactEmail, contactPhone, rentFrequency);
    }
}
