package fit.hutech.TruongGiaHuy.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootLocation = Paths.get("C:/Java/uploads");

    public StorageService() {
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(rootLocation.resolve("Books"));
            Files.createDirectories(rootLocation.resolve("Banners"));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file, String subDir) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            // Generate unique filename
            String storedFilename = UUID.randomUUID().toString() + extension;
            
            Path destinationFile = this.rootLocation.resolve(subDir).resolve(
                    Paths.get(storedFilename)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.resolve(subDir).toAbsolutePath())) {
                // Security check
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            
            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Return the relative URL path
            return "/uploads/" + subDir + "/" + storedFilename;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
}


