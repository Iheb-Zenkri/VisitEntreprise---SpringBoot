package Spring.Visit.UserModuleTest.controllersTest;

import Spring.Visit.UserModule.controllers.GroupController;
import Spring.Visit.UserModule.dto.StudentGroupDTO;
import Spring.Visit.UserModule.entities.Group;
import Spring.Visit.UserModule.services.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllGroups_ShouldReturnListOfGroups() {
        List<StudentGroupDTO> mockGroups = Arrays.asList(new StudentGroupDTO(), new StudentGroupDTO());
        when(groupService.getAllGroups()).thenReturn(mockGroups);

        ResponseEntity<List<StudentGroupDTO>> response = groupController.getAllGroups();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    void getGroupById_ShouldReturnGroup() {
        Long id = 1L;
        StudentGroupDTO mockGroup = new StudentGroupDTO();
        when(groupService.getGroupById(id)).thenReturn(mockGroup);

        ResponseEntity<StudentGroupDTO> response = groupController.getGroupById(id);

        assertNotNull(response);
        assertEquals(mockGroup, response.getBody());
        verify(groupService, times(1)).getGroupById(id);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGroup_ShouldReturnCreatedGroup() {
        String name = "Group A";
        Group mockGroup = new Group();
        when(groupService.createGroup(name)).thenReturn(mockGroup);

        ResponseEntity<Group> response = groupController.createGroup(name);

        assertNotNull(response);
        assertEquals(mockGroup, response.getBody());
        verify(groupService, times(1)).createGroup(name);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGroup_ShouldReturnUpdatedGroup() {
        Long id = 1L;
        String name = "Updated Group";
        StudentGroupDTO updatedGroup = new StudentGroupDTO();
        when(groupService.updateGroup(id, name)).thenReturn(updatedGroup);

        ResponseEntity<StudentGroupDTO> response = groupController.updateGroup(id, name);

        assertNotNull(response);
        assertEquals(updatedGroup, response.getBody());
        verify(groupService, times(1)).updateGroup(id, name);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addStudentToGroup_ShouldReturnUpdatedGroup() {
        Long groupId = 1L;
        Long studentId = 2L;
        StudentGroupDTO updatedGroup = new StudentGroupDTO();
        when(groupService.addStudentToGroup(groupId, studentId)).thenReturn(updatedGroup);

        ResponseEntity<StudentGroupDTO> response = groupController.addStudentToGroup(groupId, studentId);

        assertNotNull(response);
        assertEquals(updatedGroup, response.getBody());
        verify(groupService, times(1)).addStudentToGroup(groupId, studentId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeStudentFromGroup_ShouldReturnUpdatedGroup() {
        Long groupId = 1L;
        Long studentId = 2L;
        StudentGroupDTO updatedGroup = new StudentGroupDTO();
        when(groupService.removeStudentFromGroup(groupId, studentId)).thenReturn(updatedGroup);

        ResponseEntity<StudentGroupDTO> response = groupController.removeStudentFromGroup(groupId, studentId);

        assertNotNull(response);
        assertEquals(updatedGroup, response.getBody());
        verify(groupService, times(1)).removeStudentFromGroup(groupId, studentId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGroup_ShouldCallService() {
        Long id = 1L;
        doNothing().when(groupService).deleteGroup(id);

        ResponseEntity<Void> response = groupController.deleteGroup(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(groupService, times(1)).deleteGroup(id);
    }

}

