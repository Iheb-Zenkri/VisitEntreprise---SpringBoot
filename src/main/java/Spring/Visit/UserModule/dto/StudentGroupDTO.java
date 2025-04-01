package Spring.Visit.UserModule.dto;

import Spring.Visit.UserModule.entities.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
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
                group.getStudents() == null ? Collections.emptyList() : group.getStudents().stream().map(StudentDTO::toStudentDTO).toList()
        );
    }
}
