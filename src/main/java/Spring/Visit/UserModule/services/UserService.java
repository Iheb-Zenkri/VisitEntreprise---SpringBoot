package Spring.Visit.UserModule.services;

import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.UserModule.dto.LoginDTO;
import Spring.Visit.UserModule.dto.UpdateUserDTO;
import Spring.Visit.UserModule.dto.CreateUserDTO;
import Spring.Visit.UserModule.dto.UserDTO;
import Spring.Visit.UserModule.entities.Student;
import Spring.Visit.UserModule.entities.Teacher;
import Spring.Visit.UserModule.entities.Admin;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.enums.UserRole;
import Spring.Visit.SharedModule.exceptions.InvalidCredentialsException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.repositories.AdminRepository;
import Spring.Visit.UserModule.repositories.StudentRepository;
import Spring.Visit.UserModule.repositories.TeacherRepository;
import Spring.Visit.UserModule.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import Spring.Visit.SharedModule.utils.JwtUtil;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;


    public UserDTO registerUser(CreateUserDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("L'email existe déjà");
        }

        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRequestException("Format d'email invalide");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        dto.setFirstName(capitalize(dto.getFirstName()));
        dto.setLastName(capitalize(dto.getLastName()));
        User user = new User();

        switch (dto.getRole()) {
            case STUDENT -> user = studentRepository.save(new Student(dto));
            case TEACHER -> user = teacherRepository.save(new Teacher(dto));
            case ADMIN -> user = adminRepository.save(new Admin(dto));
            default -> throw new BadRequestException("Rôle invalide");
        }

        logger.info("User registered successfully with email: {} and role: {}", dto.getEmail(), dto.getRole());

        return modelMapper.map(user, UserDTO.class);
    }

    public Map<String, Object> authenticateUser(LoginDTO loginDTO) {
        User dbUser = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur avec l'email " + loginDTO.getEmail() + " non trouvé"));

        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), dbUser.getPassword())) {
            logger.warn("Invalid credentials for user with email: {}", loginDTO.getEmail());
            throw new InvalidCredentialsException("Email ou mot de passe invalide");
        }

        dbUser.setLastLogin(LocalDateTime.now());
        dbUser = userRepository.save(dbUser);

        String token = jwtUtil.generateToken(dbUser.getEmail(),dbUser.getRole().toString(),loginDTO.isExpiresIn30Days());
        if(loginDTO.isExpiresIn30Days()){
            logger.info("User with email {} authenticated successfully for 30 Days", loginDTO.getEmail());
        }else{
            logger.info("User with email {} authenticated successfully for 24 Hours", loginDTO.getEmail());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user",UserDTO.toUserDTO(dbUser));
        return response ;
    }

    public List<UserDTO> getAllUsers(UserRole role) {
        if (role != null) {
            logger.info("Fetching users with role: {}", role);
            return userRepository.findByRole(role)
                    .stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
        }
        return userRepository.findAll()
                .stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();

    }
    public UserDTO getUserByEmail(String email){
        logger.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return UserDTO.toUserDTO(user);
    }

    public UserDTO updateUser(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with email " + dto.getEmail() + " not found"));

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new InvalidCredentialsException("Email is already in use");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getFirstName() != null) {
            user.setFirstName(capitalize(dto.getFirstName()));
        }
        if (dto.getLastName() != null) {
            user.setLastName(capitalize(dto.getLastName()));
        }
        if (dto.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword())); // Hash password before saving
        }

        User updatedUser = userRepository.save(user);
        logger.info("User with id {} updated successfully", id);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    public String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
