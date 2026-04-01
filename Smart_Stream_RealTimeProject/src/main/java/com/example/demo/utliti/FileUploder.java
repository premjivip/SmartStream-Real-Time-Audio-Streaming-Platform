package com.example.demo.utliti;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploder {
	public static String saveFile(MultipartFile file, String uploadDir) throws IOException {
       
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

        File destinationFile = new File(uploadDir + "/" + uniqueFileName);
        file.transferTo(destinationFile);

        return uniqueFileName; 
    }
}
