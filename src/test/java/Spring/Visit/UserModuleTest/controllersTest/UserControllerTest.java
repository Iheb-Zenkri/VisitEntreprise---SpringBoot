package Spring.Visit.UserModuleTest.controllersTest;

import Spring.Visit.UserModule.controllers.UserController;
import Spring.Visit.UserModule.dto.LoginDTO;
import Spring.Visit.UserModule.dto.CreateUserDTO;
import Spring.Visit.UserModule.dto.UpdateUserDTO;
import Spring.Visit.UserModule.dto.UserDTO;
import Spring.Visit.UserModule.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnUserDTO() {
        CreateUserDTO dto = new CreateUserDTO();
        UserDTO mockUser = new UserDTO();
        when(userService.registerUser(dto)).thenReturn(mockUser);

        ResponseEntity<UserDTO> response = userController.registerUser(dto);

        assertNotNull(response);
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).registerUser(dto);
    }

    @Test
    void login_ShouldReturnToken() {
        LoginDTO loginDTO = new LoginDTO();
        String mockToken = "mockToken";
        when(userService.authenticateUser(loginDTO)).thenReturn(mockToken);

        String response = userController.login(loginDTO);

        assertEquals(mockToken, response);
        verify(userService, times(1)).authenticateUser(loginDTO);
    }
    @Test
    void getAllUsers_ShouldReturnPageOfUsers() {
        Page<UserDTO> mockPage = new PageImpl<>(List.of(new UserDTO()));
        Pageable pageable = mock(Pageable.class);
        when(userService.getAllUsers(null, pageable)).thenReturn(mockPage);

        ResponseEntity<Page<UserDTO>> response = userController.getAllUsers(null, pageable);

        assertNotNull(response);
        assertEquals(mockPage, response.getBody());
        verify(userService, times(1)).getAllUsers(null, pageable);
    }
    @Test
    void getUserByEmail_ShouldReturnUserDTO() {
        String email = "test@example.com";
        UserDTO mockUser = new UserDTO();
        when(userService.getUserByEmail(email)).thenReturn(mockUser);

        ResponseEntity<UserDTO> response = userController.getUserByEmail(email);

        assertNotNull(response);
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).getUserByEmail(email);
    }
    @Test
    void updateUser_ShouldReturnUpdatedUserDTO() {
        Long userId = 1L;
        UpdateUserDTO dto = new UpdateUserDTO();
        UserDTO mockUser = new UserDTO();
        when(userService.updateUser(userId, dto)).thenReturn(mockUser);

        ResponseEntity<UserDTO> response = userController.updateUser(userId, dto);

        assertNotNull(response);
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).updateUser(userId, dto);
    }
    @Test
    void deleteUser_ShouldReturnSuccessMessage() {
        Long userId = 1L;

        ResponseEntity<String> response = userController.deleteUser(userId);

        assertNotNull(response);
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }
}
