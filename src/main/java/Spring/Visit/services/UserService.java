package Spring.Visit.services;

import Spring.Visit.dto.LoginDTO;
import Spring.Visit.entities.User_Package.Student;
import Spring.Visit.entities.User_Package.Teacher;
import Spring.Visit.entities.User_Package.Admin;
import Spring.Visit.entities.User_Package.User;
import Spring.Visit.exceptions.InvalidCredentialsException;
import Spring.Visit.exceptions.UserNotFoundException;
import Spring.Visit.repositories.AdminRepository;
import Spring.Visit.repositories.StudentRepository;
import Spring.Visit.repositories.TeacherRepository;
import Spring.Visit.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import Spring.Visit.utils.JwtUtil;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(JwtUtil jwtUtil, UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, AdminRepository adminRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.adminRepository = adminRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User registerUser(User user) {
        // Encrypt password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        switch (user.getRole()) {
            case STUDENT -> {
                Student student = new Student(user);
                user = studentRepository.save(student);
            }
            case TEACHER -> {
                Teacher teacher = new Teacher(user);
                user = teacherRepository.save(teacher);
            }
            case ADMIN -> {
                Admin admin = new Admin(user);
                user = adminRepository.save(admin);
            }
            default -> throw new RuntimeException("Invalid role");
        }

        return user;
    }

    public String authenticateUser(LoginDTO loginDTO) {
        User dbUser = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + loginDTO.getEmail() + " not found"));

        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), dbUser.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Update lastLogin timestamp
        dbUser.setLastLogin(LocalDateTime.now());
        userRepository.save(dbUser);

        // Generate JWT token
        return jwtUtil.generateToken(dbUser.getEmail());
    }
}
