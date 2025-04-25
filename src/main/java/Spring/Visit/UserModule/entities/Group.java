package Spring.Visit.UserModule.entities;
import Spring.Visit.VisitModule.entities.Visit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "group")
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "studentGroup")
    @JsonBackReference
    private List<Visit> visits = new ArrayList<>();

    public int getNbrStudent() {
        return (students != null) ? students.size() : 0;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

