package Spring.Visit.VisitModule.Dtos;

import Spring.Visit.VisitModule.entities.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private Long id;
    private String name;
    private String address;
    private String contactEmail;
    private String contactPhone;
    private String expertiseDomain;
    private float relevanceScore;
    private int visitFrequency;

    public static CompanyDTO toCompanyDTO(Company company){
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getAddress(),
                company.getContactEmail(),
                company.getContactPhone(),
                company.getExpertiseDomain(),
                company.getRelevanceScore(),
                company.getVisitFrequency()
        );
    }
}
