package Spring.Visit.VisitModule.services;

import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.VisitModule.Dtos.CompanyDTO;
import Spring.Visit.VisitModule.entities.Company;
import Spring.Visit.VisitModule.repositories.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setAddress(company.getAddress());
        company.setName(companyDTO.getName());
        company.setContactEmail(companyDTO.getContactEmail());
        company.setContactPhone(companyDTO.getContactPhone());
        company.setExpertiseDomain(companyDTO.getExpertiseDomain());
        company.setRelevanceScore(companyDTO.getRelevanceScore());
        company.setVisitFrequency(companyDTO.getVisitFrequency());
        return CompanyDTO.toCompanyDTO(companyRepository.save(company));
    }

    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found with id: " + id));
        return CompanyDTO.toCompanyDTO(company);
    }

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream().map(CompanyDTO::toCompanyDTO).toList();
    }

    public CompanyDTO updateCompany(Long id, Company updatedCompany) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found with id: " + id));
        if(updatedCompany.getName() != null) company.setName(updatedCompany.getName());
        if(updatedCompany.getAddress() != null) company.setAddress(updatedCompany.getAddress());
        if(updatedCompany.getContactEmail() != null) company.setContactEmail(updatedCompany.getContactEmail());
        if(updatedCompany.getContactPhone() != null) company.setContactPhone(updatedCompany.getContactPhone());
        if(updatedCompany.getExpertiseDomain() != null) company.setExpertiseDomain(updatedCompany.getExpertiseDomain());
        company.setRelevanceScore(updatedCompany.getRelevanceScore());
        company.setVisitFrequency(updatedCompany.getVisitFrequency());
        return CompanyDTO.toCompanyDTO(companyRepository.save(company));
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}