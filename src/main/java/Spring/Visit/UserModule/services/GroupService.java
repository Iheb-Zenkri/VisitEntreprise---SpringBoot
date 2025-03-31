package Spring.Visit.UserModule.services;

import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.UserModule.dto.StudentGroupDTO;
import Spring.Visit.UserModule.entities.Group;
import Spring.Visit.UserModule.entities.Student;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.repositories.GroupRepository;
import Spring.Visit.UserModule.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

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

    public Group createGroup(String name) {
        if (groupRepository.findByName(name).isPresent()) {
            logger.error("Group with name '{}' already exists", name);
            throw new RuntimeException("Group with this name already exists");
        }
        Group newGroup = groupRepository.save(Group.builder().name(name).build());
        logger.info("Created new group with name '{}'", name);
        return newGroup;
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
}
