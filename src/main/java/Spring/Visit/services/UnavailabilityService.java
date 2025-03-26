package Spring.Visit.services;

import Spring.Visit.entities.Teacher;
import Spring.Visit.entities.Unavailability;
import Spring.Visit.exceptions.InvalidCredentialsException;
import Spring.Visit.exceptions.ObjectNotFoundException;
import Spring.Visit.exceptions.UserNotFoundException;
import Spring.Visit.repositories.TeacherRepository;
import Spring.Visit.repositories.UnavailabilityRepository;
import lombok.AllArgsConstructor;
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

    public Unavailability addUnavailability(Long teacherId, Unavailability unavailability) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));
        unavailability.setTeacher(teacher);
        return unavailabilityRepository.save(unavailability);
    }

    public Unavailability updateUnavailability(Long teacherId, Long unavailabilityId, Unavailability newUnavailability) {
        Unavailability unavailability = unavailabilityRepository.findById(unavailabilityId)
                .orElseThrow(() -> new ObjectNotFoundException("Unavailability not found"));

        if (teacherId.intValue() != unavailability.getTeacher().getId()) {
            throw new InvalidCredentialsException("You are not allowed to update this schedule block.");
        }

        unavailability.setStartTime(newUnavailability.getStartTime());
        unavailability.setEndTime(newUnavailability.getEndTime());

        return unavailabilityRepository.save(unavailability);
    }


    public void deleteUnavailability(Long teacherId, Long unavailabilityId) {
        Unavailability unavailability = unavailabilityRepository.findById(unavailabilityId)
                .orElseThrow(() -> new ObjectNotFoundException("Unavailability not found"));

        if ( teacherId.intValue() != unavailability.getTeacher().getId()) {
            throw new InvalidCredentialsException("You are not allowed to delete this schedule block.");
        }
        unavailabilityRepository.deleteById(unavailabilityId);
    }
}

