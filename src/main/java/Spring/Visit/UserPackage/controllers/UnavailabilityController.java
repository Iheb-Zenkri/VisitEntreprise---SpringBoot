package Spring.Visit.UserPackage.controllers;

import Spring.Visit.UserPackage.entities.Unavailability;
import Spring.Visit.SharedPackage.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedPackage.exceptions.UserNotFoundException;
import Spring.Visit.UserPackage.repositories.TeacherRepository;
import Spring.Visit.UserPackage.services.UnavailabilityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/unavailability")
public class UnavailabilityController {
    private final UnavailabilityService unavailabilityService;
    private final TeacherRepository teacherRepository;

    public UnavailabilityController(UnavailabilityService unavailabilityService, TeacherRepository teacherRepository) {
        this.unavailabilityService = unavailabilityService;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public List<Unavailability> getUnavailability(@RequestParam(required = false) Long teacherId) {
        if (teacherId == null) {
            teacherId = getAuthenticatedTeacherId();
        }
        return unavailabilityService.getUnavailability(teacherId);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public Unavailability addUnavailability(@RequestBody Unavailability unavailability) {
        Long teacherId = getAuthenticatedTeacherId();
        return unavailabilityService.addUnavailability(teacherId, unavailability);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}")
    public Unavailability updateUnavailability(@PathVariable Long id, @RequestBody Unavailability updatedUnavailability) {
        Long teacherId = getAuthenticatedTeacherId();
        return unavailabilityService.updateUnavailability(teacherId, id, updatedUnavailability);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public void deleteUnavailability(@PathVariable Long id) {
        Long teacherId = getAuthenticatedTeacherId();
        unavailabilityService.deleteUnavailability(teacherId, id);
    }

    private Long getAuthenticatedTeacherId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return (long) teacherRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("Teacher not found"))
                    .getId();
        }
        throw new InvalidCredentialsException("User is not authenticated");
    }
}

