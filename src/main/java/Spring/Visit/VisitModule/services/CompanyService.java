package Spring.Visit.VisitModule.services;

import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
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

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found with id: " + id));
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        Company company = getCompanyById(id);
        if(updatedCompany.getName() != null) company.setName(updatedCompany.getName());
        if(updatedCompany.getAddress() != null) company.setAddress(updatedCompany.getAddress());
        if(updatedCompany.getContactEmail() != null) company.setContactEmail(updatedCompany.getContactEmail());
        if(updatedCompany.getContactPhone() != null) company.setContactPhone(updatedCompany.getContactPhone());
        if(updatedCompany.getExpertiseDomain() != null) company.setExpertiseDomain(updatedCompany.getExpertiseDomain());
        company.setRelevanceScore(updatedCompany.getRelevanceScore());
        company.setVisitFrequency(updatedCompany.getVisitFrequency());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}