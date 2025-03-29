package Spring.Visit.DocumentPackage.controllers;

import Spring.Visit.DocumentPackage.dtos.ProfilePictureDTO;
import Spring.Visit.DocumentPackage.entities.ProfilePicture;
import Spring.Visit.DocumentPackage.services.ProfilePictureService;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/profile-pictures")
public class ProfilePictureController {
    private final ProfilePictureService profilePictureService;

    public ProfilePictureController(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ProfilePictureDTO> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam("userId") Long userId) {
        try {
            ProfilePictureDTO profilePicture = profilePictureService.uploadProfilePicture(file, userId);
            return ResponseEntity.ok(profilePicture);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FileSystemResource> getProfilePictureFile(@PathVariable Long userId) {
        try {
            FileSystemResource fileResource = profilePictureService.getProfilePicture(userId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(fileResource.getFile().toPath())))
                    .body(fileResource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteProfilePicture(@PathVariable Long userId) {
        try {
            if (profilePictureService.deleteProfilePicture(userId)) {
                return ResponseEntity.ok("Profile picture deleted successfully.");
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete profile picture.");
        }
    }
}
