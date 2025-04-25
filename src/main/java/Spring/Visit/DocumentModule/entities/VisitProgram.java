package Spring.Visit.DocumentModule.entities;

import Spring.Visit.UserModule.entities.User;
import Spring.Visit.VisitModule.entities.Visit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "visit_programs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne
    @JoinColumn(name = "added_by", nullable = false)
    private User addedBy;

    @ManyToOne
    @JoinColumn(name = "visit_id")
    private Visit visit;
}
