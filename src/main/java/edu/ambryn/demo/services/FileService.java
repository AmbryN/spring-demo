package edu.ambryn.demo.services;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;

@Service
public class FileService {
    @Value("${dossier.upload}")
    private String uploadPath;

    public void transferFile(MultipartFile file, String newFilename) throws IOException {
        Path fileUploadPath = Paths.get(uploadPath);

        if (!Files.exists(fileUploadPath)) {
            Files.createDirectories(fileUploadPath);
        }

        Path destination = Paths.get(uploadPath + "/" + newFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public byte[] getPictureByName(String picture) throws FileNotFoundException {
        Path destination = Paths.get(uploadPath + "/" + picture);

        try {
            return IOUtils.toByteArray(destination.toUri());
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
}
