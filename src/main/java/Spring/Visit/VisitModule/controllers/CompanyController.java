package Spring.Visit.VisitModule.controllers;

import Spring.Visit.VisitModule.Dtos.CompanyDTO;
import Spring.Visit.VisitModule.entities.Company;
import Spring.Visit.VisitModule.services.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(companyService.createCompany(companyDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        Map<String,String> response = new HashMap<>();
        response.put("message","l'entreprise a a été supprimé avec succès.");
        return ResponseEntity.ok(response);
    }
}