package Spring.Visit.DocumentModule.services;

import Spring.Visit.DocumentModule.dtos.FileStoredDto;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "SavedDocuments/";

    public FileStoredDto storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot store empty file");
        }

        // Ensure directory exists
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate unique file name to avoid collisions
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String uniqueFileName  = UUID.randomUUID() + "_" + System.currentTimeMillis() + fileExtension;
        Path destinationPath = Paths.get(UPLOAD_DIR, uniqueFileName );

        // Copy file to storage directory
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // get file type
        String mimeType = Files.probeContentType(destinationPath);

        return new FileStoredDto(uniqueFileName , mimeType,destinationPath);
    }

    public FileSystemResource getFile(Path filePath) throws IOException {

        if (!Files.exists(filePath)) {
            throw new IOException("File not found in " + filePath.toString());
        }

        return new FileSystemResource(filePath.toFile());
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.deleteIfExists(filePath);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
