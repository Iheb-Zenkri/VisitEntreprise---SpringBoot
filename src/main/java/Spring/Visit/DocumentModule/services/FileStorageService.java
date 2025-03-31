package Spring.Visit.DocumentModule.services;

import Spring.Visit.DocumentModule.dtos.FileStoredDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private static final String UPLOAD_DIR = "SavedDocuments/";

    public FileStoredDto storeFile(MultipartFile file) throws IOException {
        logger.info("Start storing file with name: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            logger.error("Attempted to store an empty file");
            throw new IllegalStateException("Cannot store empty file");
        }

        // Ensure directory exists
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            logger.info("Directory does not exist, creating: {}", UPLOAD_DIR);
            directory.mkdirs();
        }

        // Generate unique file name to avoid collisions
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String uniqueFileName  = UUID.randomUUID() + "_" + System.currentTimeMillis() + fileExtension;
        Path destinationPath = Paths.get(UPLOAD_DIR, uniqueFileName);

        // Copy file to storage directory
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File stored successfully at path: {}", destinationPath.toString());

        // Get file type
        String mimeType = Files.probeContentType(destinationPath);
        logger.info("File mime type: {}", mimeType);

        return new FileStoredDto(uniqueFileName, mimeType, destinationPath);
    }

    public FileSystemResource getFile(Path filePath) throws IOException {
        logger.info("Fetching file from path: {}", filePath.toString());

        if (!Files.exists(filePath)) {
            logger.error("File not found at path: {}", filePath.toString());
            throw new IOException("File not found in " + filePath.toString());
        }

        FileSystemResource fileResource = new FileSystemResource(filePath.toFile());
        logger.info("File successfully retrieved from path: {}", filePath.toString());
        return fileResource;
    }

    public void deleteFile(String fileName) throws IOException {
        logger.info("Attempting to delete file with name: {}", fileName);

        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        if (!Files.exists(filePath)) {
            logger.error("File to delete not found at path: {}", filePath.toString());
            throw new IOException("File not found to delete: " + fileName);
        }

        Files.deleteIfExists(filePath);
        logger.info("File with name: {} deleted successfully from path: {}", fileName, filePath.toString());
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
