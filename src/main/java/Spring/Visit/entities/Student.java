package Spring.Visit.entities;

import Spring.Visit.dto.CreateUserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Student extends User {
    public Student(CreateUserDTO dto) {
        super(dto);
    }

    @ManyToOne
    @JoinColumn(name="group_id")
    @JsonBackReference
    private Group group;
}
