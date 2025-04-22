package Spring.Visit.DocumentModule.entities;

import Spring.Visit.UserModule.entities.User;
import Spring.Visit.VisitModule.entities.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company_pictures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @OneToOne
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Company company;
}
