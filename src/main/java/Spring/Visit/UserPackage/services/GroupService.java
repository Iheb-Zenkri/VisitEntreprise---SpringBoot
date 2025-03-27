package Spring.Visit.UserPackage.services;

import Spring.Visit.UserPackage.dto.StudentGroupDTO;
import Spring.Visit.UserPackage.entities.Group;
import Spring.Visit.UserPackage.entities.Student;
import Spring.Visit.SharedPackage.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedPackage.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedPackage.exceptions.UserNotFoundException;
import Spring.Visit.UserPackage.repositories.GroupRepository;
import Spring.Visit.UserPackage.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public List<StudentGroupDTO> getAllGroups() {
        return groupRepository.findAll().stream().map(StudentGroupDTO::toStudentGroupDTO).toList();
    }

    public StudentGroupDTO getGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        return StudentGroupDTO.toStudentGroupDTO(group);
    }

    public Group createGroup(String name) {
        if (groupRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Group with this name already exists");
        }
        return groupRepository.save(Group.builder().name(name).build());
    }

    public StudentGroupDTO updateGroup(Long id,String name){
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Group not found"));

        if(name.isBlank()) throw new InvalidCredentialsException("the new Group Name is empty!");
        group.setName(name);
         return StudentGroupDTO.toStudentGroupDTO(groupRepository.save(group));
    }
    @Transactional
    public StudentGroupDTO addStudentToGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ObjectNotFoundException("Group not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if (student.getGroup() != null) {
            throw new RuntimeException("Student is already assigned to another group.");
        }
        student.setGroup(group);
        group.getStudents().add(student);
        studentRepository.save(student);

        Group newGroup = groupRepository.save(group);
        return StudentGroupDTO.toStudentGroupDTO(newGroup);
    }

    @Transactional
    public StudentGroupDTO removeStudentFromGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ObjectNotFoundException("Group not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if (student.getGroup() != group) {
            throw new RuntimeException("Student is not in this group");
        }

        student.setGroup(null);
        group.getStudents().remove(student);
        studentRepository.save(student);

        Group newGroup = groupRepository.save(group) ;
        return StudentGroupDTO.toStudentGroupDTO(newGroup);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

}

