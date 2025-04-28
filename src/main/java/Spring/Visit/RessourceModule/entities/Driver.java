package Spring.Visit.RessourceModule.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "license_number", nullable = false, unique = true)
    private long licenseNumber;

    @Column(name = "contact_phone", nullable = false, unique = true)
    private long contactPhone;

    @OneToOne(mappedBy = "driver")
    private Bus bus;

    public Driver(String name, Long licenseNumber, Long contactPhone) {
        this.name=name;
        this.licenseNumber=licenseNumber;
        this.contactPhone=contactPhone;
    }
}
