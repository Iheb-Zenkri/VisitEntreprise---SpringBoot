package Spring.Visit.UserModuleTest.servicesTest;

import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.UserModule.dto.StudentGroupDTO;
import Spring.Visit.UserModule.entities.Group;
import Spring.Visit.UserModule.entities.Student;
import Spring.Visit.UserModule.repositories.GroupRepository;
import Spring.Visit.UserModule.repositories.StudentRepository;
import Spring.Visit.UserModule.services.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @Test
    void testGetAllGroups() {
        Group group = new Group();
        group.setId(1L);
        group.setName("Group A");
        groupRepository.save(group);
        when(groupRepository.findAll()).thenReturn(List.of(group));

        List<StudentGroupDTO> result = groupService.getAllGroups();
        System.out.println("results : "+result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Group A", result.get(0).getName());

        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void testGetGroupById_Success() {
        Group group = new Group();
        group.setId(1L);
        group.setName("Group A");
        groupRepository.save(group);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        StudentGroupDTO result = groupService.getGroupById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Group A", result.getName());

        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    void testGetGroupById_NotFound() {
        Group group = new Group();
        group.setId(1L);
        group.setName("Group A");
        groupRepository.save(group);
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> groupService.getGroupById(1L));

        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateGroup_Success() {
        String groupName = "New Group";
        Group group = new Group();
        group.setName(groupName);

        when(groupRepository.findByName(groupName)).thenReturn(Optional.empty());
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group createdGroup = groupService.createGroup(groupName);

        assertNotNull(createdGroup);
        assertEquals(groupName, createdGroup.getName());

        verify(groupRepository, times(1)).findByName(groupName);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testCreateGroup_AlreadyExists() {
        String groupName = "Existing Group";
        when(groupRepository.findByName(groupName)).thenReturn(Optional.of(new Group()));

        assertThrows(RuntimeException.class, () -> groupService.createGroup(groupName));

        verify(groupRepository, times(1)).findByName(groupName);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void testUpdateGroup_Success() {
        Group group = new Group();
        group.setId(1L);
        group.setName("Old Name");

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        StudentGroupDTO updatedGroup = groupService.updateGroup(1L, "New Name");

        assertNotNull(updatedGroup);
        assertEquals("New Name", updatedGroup.getName());

        verify(groupRepository, times(1)).findById(1L);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testUpdateGroup_NotFound() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> groupService.updateGroup(1L, "New Name"));

        verify(groupRepository, times(1)).findById(1L);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void testUpdateGroup_EmptyName() {
        Group group = new Group();
        group.setId(1L);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        assertThrows(BadRequestException.class, () -> groupService.updateGroup(1L, ""));

        verify(groupRepository, times(1)).findById(1L);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void testAddStudentToGroup_Success() {
        Group group = new Group();
        group.setId(1L);
        Student student = new Student();
        student.setId(1L);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        StudentGroupDTO result = groupService.addStudentToGroup(1L, 1L);

        assertNotNull(result);
        verify(groupRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).findById(1L);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testAddStudentToGroup_StudentAlreadyAssigned() {
        Group group = new Group();
        group.setId(1L);
        Student student = new Student();
        student.setId(1L);
        student.setGroup(new Group()); // Already assigned to a group

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertThrows(RuntimeException.class, () -> groupService.addStudentToGroup(1L, 1L));

        verify(groupRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).findById(1L);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void testRemoveStudentFromGroup_Success() {
        Group group = new Group();
        group.setId(1L);
        Student student = new Student();
        student.setId(1L);
        student.setGroup(group);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        StudentGroupDTO result = groupService.removeStudentFromGroup(1L, 1L);

        assertNotNull(result);
        verify(groupRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).findById(1L);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testRemoveStudentFromGroup_StudentNotInGroup() {
        Group group = new Group();
        group.setId(1L);
        Student student = new Student();
        student.setId(1L);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertThrows(RuntimeException.class, () -> groupService.removeStudentFromGroup(1L, 1L));

        verify(groupRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).findById(1L);
        verify(groupRepository, never()).save(any(Group.class));
    }

    @Test
    void testDeleteGroup_Success() {
        doNothing().when(groupRepository).deleteById(1L);

        groupService.deleteGroup(1L);

        verify(groupRepository, times(1)).deleteById(1L);
    }
}
