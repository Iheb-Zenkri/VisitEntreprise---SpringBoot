package Spring.Visit.UserModuleTest.servicesTest;
import Spring.Visit.UserModule.dto.CreateUserDTO;
import Spring.Visit.UserModule.dto.LoginDTO;
import Spring.Visit.UserModule.dto.UpdateUserDTO;
import Spring.Visit.UserModule.dto.UserDTO;
import Spring.Visit.UserModule.entities.Student;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.enums.UserRole;
import Spring.Visit.UserModule.repositories.AdminRepository;
import Spring.Visit.UserModule.repositories.StudentRepository;
import Spring.Visit.UserModule.repositories.TeacherRepository;
import Spring.Visit.UserModule.repositories.UserRepository;
import Spring.Visit.SharedModule.utils.JwtUtil;
import Spring.Visit.UserModule.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private UserRepository userRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private AdminRepository adminRepository;
    @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock private ModelMapper modelMapper;
    @InjectMocks private UserService userService;


    @Test
    void registerUser_ShouldRegisterUserSuccessfully() {
        // Given
        CreateUserDTO dto = new CreateUserDTO("John", "Doe", "john@example.com", "password123", UserRole.STUDENT);
        Student user = new Student(dto);
        UserDTO userDTO = new UserDTO(100L, "John", "Doe", "john@example.com", UserRole.STUDENT,null,null,null);

        when(studentRepository.save(any(Student.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO);
        when(bCryptPasswordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        // When
        UserDTO result = userService.registerUser(dto);

        // Then
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getRole(), result.getRole());
    }

    @Test
    void authenticateUser_ShouldReturnToken_WhenCredentialsAreValid() {
        // Given
        LoginDTO loginDTO = new LoginDTO("john@example.com", "password123");
        User dbUser = new Student(new CreateUserDTO("John", "Doe", "john@example.com", "password123", UserRole.STUDENT));
        dbUser.setPassword("encodedPassword");
        String token = "jwt-token";

        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(java.util.Optional.of(dbUser));
        when(bCryptPasswordEncoder.matches(loginDTO.getPassword(), dbUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(dbUser.getEmail())).thenReturn(token);

        // When
        String result = userService.authenticateUser(loginDTO);

        // Then
        assertEquals(token, result);
    }

    @Test
    void getAllUsers_ShouldReturnUsers_WhenRoleIsProvided() {
        // Given
        Pageable pageable = Pageable.unpaged();

        List<User> userList = List.of(
                new Student(new CreateUserDTO("John", "Doe", "john@example.com", "password123", UserRole.STUDENT))
        );
        Page<User> usersPage = new PageImpl<>(userList);

        List<UserDTO> userDTOList = List.of(new UserDTO(1L, "John", "Doe", "john@example.com", UserRole.STUDENT, null, null, null));
        Page<UserDTO> userDTOPage = new PageImpl<>(userDTOList);

        when(userRepository.findByRole(UserRole.STUDENT, pageable)).thenReturn(usersPage);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTOList.get(0));

        // When
        Page<UserDTO> result = userService.getAllUsers(UserRole.STUDENT, pageable);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(userDTOList.get(0).getEmail(), result.getContent().get(0).getEmail());
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        // Given
        String email = "john@example.com";
        User user = new User(new CreateUserDTO("John", "Doe", "john@example.com", "password123", UserRole.STUDENT));
        UserDTO userDTO = new UserDTO(100L, "John", "Doe", "john@example.com", UserRole.STUDENT,null,null,null);

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));

        // When
        UserDTO result = userService.getUserByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_ShouldUpdateUserSuccessfully() {
        // Given
        Long id = 100L;
        UpdateUserDTO dto = new UpdateUserDTO("John", "Doe", "john@example.com", "newPassword");
        User user = new Student(new CreateUserDTO("John", "Doe", "john@example.com", "password123", UserRole.STUDENT));
        user.setId(id);
        UserDTO userDTO = new UserDTO(id, "John", "Doe", "john@example.com", UserRole.STUDENT,null,null,null);

        when(userRepository.findById(id)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // When
        UserDTO result = userService.updateUser(id, dto);

        // Then
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void deleteUser_ShouldDeleteUserSuccessfully() {
        // Given
        Long id = 100L;

        // When
        userService.deleteUser(id);

        // Then
        verify(userRepository, times(1)).deleteById(id);
    }
}

