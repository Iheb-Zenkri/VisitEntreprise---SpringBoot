package Spring.Visit.RessourceModule.dto;

import Spring.Visit.RessourceModule.entities.Agency;
import Spring.Visit.RessourceModule.services.AgencyService;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgencyDTO {
    private Long id;
    private String name;
    private String address;
    private String contactEmail;
    private String contactPhone;
    private double rentFrequency;

    public static AgencyDTO toAgencyDTO(Agency agency){
        return new AgencyDTO(
                agency.getId(),
                agency.getName(),
                agency.getAddress(),
                agency.getContactEmail(),
                agency.getContactPhone(),
                agency.getRentFrequency()
        );
    }
}
