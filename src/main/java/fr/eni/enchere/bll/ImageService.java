package fr.eni.enchere.bll;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	public String saveImageToStorage(String uploadDirectory, MultipartFile imageFile) throws IOException;
	
	String deleteImage(String imageDirectory) throws IOException;
	
}
