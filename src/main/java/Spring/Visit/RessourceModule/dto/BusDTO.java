package Spring.Visit.RessourceModule.dto;

import Spring.Visit.RessourceModule.entities.Bus;
import Spring.Visit.RessourceModule.enums.BusAvailability;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusDTO {

    private Long id;

    @NotBlank(message = "La capacitie du bus est obligatoire")
    private int capacity;

    @NotBlank(message = "Le numéro d'immatriculation est obligatoire")
    private String registrationNumber;

    @NotNull(message = "La disponibilité est obligatoire")
    private BusAvailability availability;

    private DriverDTO driverDTO;

    private AgencyDTO agencyDTO;

    public static BusDTO toBusDTO(Bus bus){
        return new BusDTO(
                bus.getId(),
                bus.getCapacity(),
                bus.getLicensePlate(),
                bus.getAvailability(),
                bus.getDriver() == null ? new DriverDTO() : DriverDTO.toDriverDTO(bus.getDriver()),
                bus.getAgency() == null ? new AgencyDTO() : AgencyDTO.toAgencyDTO(bus.getAgency())
        );
    }
}
