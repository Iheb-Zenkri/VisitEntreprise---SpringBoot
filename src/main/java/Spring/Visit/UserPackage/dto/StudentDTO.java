package Spring.Visit.UserPackage.dto;

import Spring.Visit.UserPackage.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime lastLogin;

    public static StudentDTO toStudentDTO(Student student){
        return new StudentDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getLastLogin()
        );
    }
}
