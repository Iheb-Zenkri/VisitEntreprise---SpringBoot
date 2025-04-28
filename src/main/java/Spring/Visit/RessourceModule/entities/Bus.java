package Spring.Visit.RessourceModule.entities;

import Spring.Visit.RessourceModule.enums.BusAvailability;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bus")
public class Bus {

    @Id
    @SequenceGenerator(name = "bus_sequence",sequenceName = "bus_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Enumerated(EnumType.STRING) // Stores enum as String in DB
    @Column(name = "availability", nullable = false)
    private BusAvailability availability;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;


}
