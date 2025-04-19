package Spring.Visit.UserModule.entities;

import Spring.Visit.UserModule.dto.CreateUserDTO;
import Spring.Visit.VisitModule.entities.Visit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Teacher extends User {
    public Teacher(CreateUserDTO user){
        super(user);
    }

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Unavailability> unavailability;

    @OneToMany(mappedBy = "responsible")
    @JsonBackReference
    private List<Visit> visits = new ArrayList<>();

}
