package Spring.Visit.dto;

import Spring.Visit.entities.Group;
import Spring.Visit.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupDTO {
    private Long id ;
    private String name ;
    private List<StudentDTO> students ;

    public static StudentGroupDTO toStudentGroupDTO(Group group){
        return new StudentGroupDTO(
                group.getId(),
                group.getName(),
                group.getStudents().stream().map(StudentDTO::toStudentDTO).toList()
        );
    }
}
