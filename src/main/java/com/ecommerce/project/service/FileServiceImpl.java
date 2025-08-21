package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile image) {


        String originalFileName = image.getOriginalFilename();


        String randomId = UUID.randomUUID().toString();


        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));

        String filePath = path + File.separator + fileName;

        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            Files.copy(image.getInputStream(), Paths.get(filePath));
            System.out.println("Image uploaded successfully: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("Error uploading image: " + e.getMessage());
        }


        return fileName;


    }

}
