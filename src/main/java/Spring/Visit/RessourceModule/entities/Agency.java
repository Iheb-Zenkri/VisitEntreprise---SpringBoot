package Spring.Visit.RessourceModule.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agency")
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;
    @Email
    @Column(name = "contact_email", nullable = false, unique = true)
    private String contactEmail;

    @Column(name = "contact_phone", nullable = false, unique = true)
    private String contactPhone;

    @Column(name = "rent_frequency", nullable = false)
    private double rentFrequency;

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL)
    private List<Bus> buses = new ArrayList<>();

}
