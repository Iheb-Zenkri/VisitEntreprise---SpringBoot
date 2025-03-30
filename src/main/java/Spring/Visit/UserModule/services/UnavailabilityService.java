package Spring.Visit.UserModule.services;

import Spring.Visit.UserModule.entities.Unavailability;
import Spring.Visit.UserModule.entities.Teacher;
import Spring.Visit.SharedModule.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.repositories.TeacherRepository;
import Spring.Visit.UserModule.repositories.UnavailabilityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class UnavailabilityService {
    private final UnavailabilityRepository unavailabilityRepository;
    private final TeacherRepository teacherRepository;


    public List<Unavailability> getUnavailability(Long teacherId) {
        return unavailabilityRepository.findByTeacherId(teacherId);
    }

    public Unavailability addUnavailability( Unavailability unavailability) {
        Long teacherId = getAuthenticatedTeacherId() ;
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));
        unavailability.setTeacher(teacher);
        return unavailabilityRepository.save(unavailability);
    }

    public Unavailability updateUnavailability(Long unavailabilityId, Unavailability newUnavailability) {
        Long teacherId = getAuthenticatedTeacherId() ;
        Unavailability unavailability = unavailabilityRepository.findById(unavailabilityId)
                .orElseThrow(() -> new ObjectNotFoundException("Unavailability not found"));

        if (teacherId.intValue() != unavailability.getTeacher().getId()) {
            throw new InvalidCredentialsException("You are not allowed to update this schedule block.");
        }

        unavailability.setStartTime(newUnavailability.getStartTime());
        unavailability.setEndTime(newUnavailability.getEndTime());

        return unavailabilityRepository.save(unavailability);
    }


    public void deleteUnavailability(Long unavailabilityId) {
        Long teacherId = getAuthenticatedTeacherId() ;

        Unavailability unavailability = unavailabilityRepository.findById(unavailabilityId)
                .orElseThrow(() -> new ObjectNotFoundException("Unavailability not found"));

        if ( teacherId.intValue() != unavailability.getTeacher().getId()) {
            throw new InvalidCredentialsException("You are not allowed to delete this schedule block.");
        }
        unavailabilityRepository.deleteById(unavailabilityId);
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

