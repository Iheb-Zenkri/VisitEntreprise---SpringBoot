package Spring.Visit.DocumentModule.controllers;

import Spring.Visit.DocumentModule.dtos.CompanyPictureDTO;
import Spring.Visit.DocumentModule.services.CompanyPictureService;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/company-pictures")
public class CompanyPictureController {

    private final CompanyPictureService companyPictureService;

    public CompanyPictureController(CompanyPictureService companyPictureService) {
        this.companyPictureService = companyPictureService;
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<FileSystemResource> getCompanyPicture(@PathVariable Long companyId) {
        try {
            FileSystemResource fileResource = companyPictureService.getCompanyPicture(companyId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(fileResource.getFile().toPath())))
                    .body(fileResource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 🔴 Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{companyId}")
    public ResponseEntity<CompanyPictureDTO> uploadCompanyPicture(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long companyId) throws IOException {
        CompanyPictureDTO dto = companyPictureService.uploadCompanyPicture(file, companyId);
        return ResponseEntity.ok(dto);
    }

    // 🔴 Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompanyPicture(@PathVariable Long companyId) throws IOException {
        companyPictureService.deleteCompanyPicture(companyId);
        return ResponseEntity.noContent().build();
    }
}

