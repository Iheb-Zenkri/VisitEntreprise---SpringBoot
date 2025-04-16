package Spring.Visit.VisitModule.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Company name is required")
    private String name;

    @NotNull(message = "Address is required")
    private String adresse;

    private String contactEmail;
    private String contactPhone;

    @NotNull(message = "Expertise domain is required")
    private String expertiseDomain;

    private float relevanceScore;
    private int visitFrequency;

    @OneToMany(mappedBy = "company")
    private List<Visit> visits;
}