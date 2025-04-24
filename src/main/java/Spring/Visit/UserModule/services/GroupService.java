package Spring.Visit.UserModule.services;

import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.UserModule.dto.StudentGroupDTO;
import Spring.Visit.UserModule.entities.Group;
import Spring.Visit.UserModule.entities.Student;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.repositories.GroupRepository;
import Spring.Visit.UserModule.repositories.StudentRepository;
import Spring.Visit.VisitModule.Dtos.VisitDTO;
import Spring.Visit.VisitModule.entities.Visit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public List<StudentGroupDTO> getAllGroups() {
        logger.info("Fetching all groups");
        return groupRepository.findAll().stream().map(StudentGroupDTO::toStudentGroupDTO).toList();
    }

    public StudentGroupDTO getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Group with ID {} not found", id);
                    return new RuntimeException("Group not found");
                });
        logger.info("Fetching group with ID {}", id);
        return StudentGroupDTO.toStudentGroupDTO(group);
    }

    public StudentGroupDTO createGroup(String name) {
        if (groupRepository.findByName(name).isPresent()) {
            logger.error("Group with name '{}' already exists", name);
            throw new RuntimeException("Group with this name already exists");
        }
        Group newGroup = groupRepository.save(Group.builder().name(name).build());
        logger.info("Created new group with name '{}'", name);
        return StudentGroupDTO.toStudentGroupDTO(newGroup);
    }

    public StudentGroupDTO updateGroup(Long id, String name) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Group with ID {} not found", id);
                    return new ObjectNotFoundException("Group not found");
                });

        if (name.isBlank()) {
            logger.error("Attempt to update group with empty name");
            throw new BadRequestException("The new group name is empty!");
        }

        group.setName(name);
        Group updatedGroup = groupRepository.save(group);
        logger.info("Updated group with ID {} to new name '{}'", id, name);
        return StudentGroupDTO.toStudentGroupDTO(updatedGroup);
    }

    @Transactional
    public StudentGroupDTO addStudentToGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> {
                    logger.error("Group with ID {} not found", groupId);
                    return new ObjectNotFoundException("Group not found");
                });
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student with ID {} not found", studentId);
                    return new UserNotFoundException("Student not found");
                });

        if (student.getGroup() != null) {
            logger.error("Student with ID {} is already assigned to another group", studentId);
            throw new RuntimeException("Student is already assigned to another group.");
        }

        student.setGroup(group);
        group.getStudents().add(student);
        studentRepository.save(student);

        Group newGroup = groupRepository.save(group);
        logger.info("Added student with ID {} to group with ID {}", studentId, groupId);
        return StudentGroupDTO.toStudentGroupDTO(newGroup);
    }

    @Transactional
    public StudentGroupDTO removeStudentFromGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> {
                    logger.error("Group with ID {} not found", groupId);
                    return new ObjectNotFoundException("Group not found");
                });
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student with ID {} not found", studentId);
                    return new UserNotFoundException("Student not found");
                });

        if (student.getGroup() != group) {
            logger.error("Student with ID {} is not in group with ID {}", studentId, groupId);
            throw new RuntimeException("Student is not in this group");
        }

        student.setGroup(null);
        group.getStudents().remove(student);
        studentRepository.save(student);

        Group newGroup = groupRepository.save(group);
        logger.info("Removed student with ID {} from group with ID {}", studentId, groupId);
        return StudentGroupDTO.toStudentGroupDTO(newGroup);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
        logger.info("Deleted group with ID {}", id);
    }

    public List<VisitDTO> getStudentVisits(Long studentId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if(student.getGroup() != null && student.getGroup().getVisits() != null){
            return student.getGroup().getVisits().stream().map(VisitDTO::toVisitDTO).toList();
        }
        return new ArrayList<>() ;
    }

    public Map<String, Object> getStudentStats(Long studentId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        Map<String, Object> response = new HashMap<>();

        List<Visit> visits = Optional.ofNullable(student.getGroup())
                .map(Group::getVisits)
                .orElse(Collections.emptyList());

        LocalDateTime now = LocalDate.now().atStartOfDay();

        long upcomingCount = visits.stream()
                .filter(visit -> visit.getVisitDate() != null && visit.getVisitDate().isAfter(now))
                .count();

        long visitedCount = visits.stream()
                .filter(visit -> visit.getVisitDate() != null && visit.getVisitDate().isBefore(now))
                .count();

        response.put("upcomingVisits", upcomingCount);
        response.put("visited", visitedCount);

        long feedbacksCount = Optional.ofNullable(student.getUsersFeedbacks())
                .map(List::size)
                .orElse(0);

        response.put("feedbacks", feedbacksCount);



        return response;
    }

    public StudentGroupDTO getStudentGroup(Long studentId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if(student.getGroup() == null){
            throw new ObjectNotFoundException("Student has no Group");
        }

        return StudentGroupDTO.toStudentGroupDTO(student.getGroup());
    }
}
