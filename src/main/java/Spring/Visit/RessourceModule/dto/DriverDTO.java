package Spring.Visit.RessourceModule.dto;

import Spring.Visit.RessourceModule.entities.Driver;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotNull(message = "Le numéro de permis est obligatoire")
    private Long licenseNumber;

    @NotNull(message = "Le numéro de téléphone est obligatoire")
    private Long contactPhone;

    public static DriverDTO toDriverDTO(Driver driver){
        return new DriverDTO(
                driver.getId(),
                driver.getName(),
                driver.getLicenseNumber(),
                driver.getContactPhone()
        );
    }
}
