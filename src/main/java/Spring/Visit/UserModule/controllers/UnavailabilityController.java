package Spring.Visit.UserModule.controllers;

import Spring.Visit.UserModule.entities.Unavailability;
import Spring.Visit.UserModule.services.UnavailabilityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/unavailability")
public class UnavailabilityController {
    private final UnavailabilityService unavailabilityService;
    public UnavailabilityController(UnavailabilityService unavailabilityService) {
        this.unavailabilityService = unavailabilityService;
    }

    @GetMapping
    public List<Unavailability> getUnavailability(@RequestParam(required = false) Long teacherId) {
        return unavailabilityService.getUnavailability(teacherId);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public Unavailability addUnavailability(@RequestBody Unavailability unavailability) {
        return unavailabilityService.addUnavailability(unavailability);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}")
    public Unavailability updateUnavailability(@PathVariable Long id, @RequestBody Unavailability updatedUnavailability) {
        return unavailabilityService.updateUnavailability(id, updatedUnavailability);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public void deleteUnavailability(@PathVariable Long id) {
        unavailabilityService.deleteUnavailability(id);
    }


}

