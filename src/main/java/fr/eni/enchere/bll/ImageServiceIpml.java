package fr.eni.enchere.bll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceIpml implements ImageService {

	@Override
	public String saveImageToStorage(String uploadDirectory, MultipartFile imageFile) throws IOException {

		String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
		
		Path uploadPath = Path.of(uploadDirectory);
		Path filePath = uploadPath.resolve(uniqueFileName);
		
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		uniqueFileName = "images\\articles\\" + uniqueFileName;
		
		Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		return uniqueFileName;
	}
	
	public String deleteImage(String imageDirectory) throws IOException {
        Path imagePath = Paths.get(imageDirectory);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return "Success";
        } else {
            return "Failed";
        }
    }
	
}
