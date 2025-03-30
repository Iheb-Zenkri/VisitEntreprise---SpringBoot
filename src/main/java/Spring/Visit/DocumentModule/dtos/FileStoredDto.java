package Spring.Visit.DocumentModule.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;

@Data
@AllArgsConstructor
public class FileStoredDto {
    private String fileName;
    private String mimeType;
    private Path filePath;
}
