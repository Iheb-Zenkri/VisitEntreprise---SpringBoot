package Spring.Visit.RessourceModule.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotNull(message = "Le numéro de permis est obligatoire")
    private Long licenseNumber;

    @NotNull(message = "Le numéro de téléphone est obligatoire")
    private Long contactPhone;
}
