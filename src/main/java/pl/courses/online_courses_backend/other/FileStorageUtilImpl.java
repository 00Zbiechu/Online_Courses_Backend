package pl.courses.online_courses_backend.other;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileStorageUtilImpl implements FileStorageUtil{

    @Override
    public String saveFile(MultipartFile multipartFile)
            throws IOException {

        //Save directory
        Path uploadPath = Paths.get("../online-trainig-project/src/assets/course_icon");

        //create directory
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        //create file code name
        String fileCode = RandomStringUtils.randomAlphanumeric(16)+".jpg";

        //copy input stream to save directory with code name instead of original file name
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file");
        }

        return fileCode;
    }
}
