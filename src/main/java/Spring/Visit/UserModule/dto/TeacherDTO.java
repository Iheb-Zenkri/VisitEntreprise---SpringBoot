package Spring.Visit.UserModule.dto;

import Spring.Visit.UserModule.entities.Student;
import Spring.Visit.UserModule.entities.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime lastLogin;

    public static TeacherDTO toTeacherDTO(Teacher teacher){
        return new TeacherDTO(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getLastLogin()
        );
    }
}
