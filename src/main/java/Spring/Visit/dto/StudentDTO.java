package Spring.Visit.dto;

import Spring.Visit.entities.Student;
import Spring.Visit.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private int id;
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
