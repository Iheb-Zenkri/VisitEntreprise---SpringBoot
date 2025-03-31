package Spring.Visit.UserModule.services;

import Spring.Visit.UserModule.entities.Unavailability;
import Spring.Visit.UserModule.entities.Teacher;
import Spring.Visit.SharedModule.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.repositories.TeacherRepository;
import Spring.Visit.UserModule.repositories.UnavailabilityRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class UnavailabilityService {

    private static final Logger logger = LoggerFactory.getLogger(UnavailabilityService.class);

    private final UnavailabilityRepository unavailabilityRepository;
    private final TeacherRepository teacherRepository;

    public List<Unavailability> getUnavailability(Long teacherId) {
        logger.info("Fetching unavailability records for teacher with ID {}", teacherId);
        List<Unavailability> unavailabilities = unavailabilityRepository.findByTeacherId(teacherId);
        if (unavailabilities.isEmpty()) {
            logger.warn("No unavailability records found for teacher with ID {}", teacherId);
        }
        return unavailabilities;
    }

    public Unavailability addUnavailability(Unavailability unavailability) {
        Long teacherId = getAuthenticatedTeacherId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> {
                    logger.error("Teacher with ID {} not found", teacherId);
                    return new UserNotFoundException("Teacher not found");
                });

        unavailability.setTeacher(teacher);
        Unavailability savedUnavailability = unavailabilityRepository.save(unavailability);
        logger.info("Added new unavailability for teacher with ID {}", teacherId);
        return savedUnavailability;
    }

    public Unavailability updateUnavailability(Long unavailabilityId, Unavailability newUnavailability) {
        Long teacherId = getAuthenticatedTeacherId();
        Unavailability unavailability = unavailabilityRepository.findById(unavailabilityId)
                .orElseThrow(() -> {
                    logger.error("Unavailability with ID {} not found", unavailabilityId);
                    return new ObjectNotFoundException("Unavailability not found");
                });

        if (teacherId.intValue() != unavailability.getTeacher().getId()) {
            logger.error("Teacher with ID {} attempted to update unavailability for another teacher", teacherId);
            throw new InvalidCredentialsException("You are not allowed to update this schedule block.");
        }

        unavailability.setStartTime(newUnavailability.getStartTime());
        unavailability.setEndTime(newUnavailability.getEndTime());
        Unavailability updatedUnavailability = unavailabilityRepository.save(unavailability);

        logger.info("Updated unavailability for teacher with ID {}", teacherId);
        return updatedUnavailability;
    }

    public void deleteUnavailability(Long unavailabilityId) {
        Long teacherId = getAuthenticatedTeacherId();
        Unavailability unavailability = unavailabilityRepository.findById(unavailabilityId)
                .orElseThrow(() -> {
                    logger.error("Unavailability with ID {} not found", unavailabilityId);
                    return new ObjectNotFoundException("Unavailability not found");
                });

        if (teacherId.intValue() != unavailability.getTeacher().getId()) {
            logger.error("Teacher with ID {} attempted to delete unavailability for another teacher", teacherId);
            throw new InvalidCredentialsException("You are not allowed to delete this schedule block.");
        }

        unavailabilityRepository.deleteById(unavailabilityId);
        logger.info("Deleted unavailability with ID {} for teacher with ID {}", unavailabilityId, teacherId);
    }

    private Long getAuthenticatedTeacherId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Teacher teacher = teacherRepository.findByEmail(username)
                    .orElseThrow(() -> {
                        logger.error("Teacher with email {} not found", username);
                        return new UserNotFoundException("Teacher not found");
                    });
            logger.info("Authenticated teacher with email {}", username);
            return teacher.getId();
        }
        logger.error("User is not authenticated");
        throw new InvalidCredentialsException("User is not authenticated");
    }
}
