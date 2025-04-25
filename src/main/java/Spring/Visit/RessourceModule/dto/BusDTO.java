package Spring.Visit.RessourceModule.dto;

import Spring.Visit.RessourceModule.enums.BusAvailability;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusDTO {

    @NotBlank(message = "Le nom du bus est obligatoire")
    private String name;

    @NotBlank(message = "Le numéro d'immatriculation est obligatoire")
    private String registrationNumber;

    @NotNull(message = "La disponibilité est obligatoire")
    private BusAvailability availability;

    @NotNull(message = "L'ID de l'agence est obligatoire")
    private Long agencyId;
}
