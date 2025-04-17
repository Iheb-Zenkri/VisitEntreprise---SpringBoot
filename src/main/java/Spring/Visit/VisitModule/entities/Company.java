package Spring.Visit.VisitModule.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private String address;

    @NotNull(message = "Email is required")
    private String contactEmail;
    private String contactPhone;

    @NotNull(message = "Expertise domain is required")
    private String expertiseDomain;

    @Size(message = "Le Score doit etre entre 0 et 100",min = 0,max = 100)
    private float relevanceScore;

    @Size(message = "La fréquence doit etre positive",min = 0)
    private int visitFrequency;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    private List<Visit> visits = new ArrayList<>();
}