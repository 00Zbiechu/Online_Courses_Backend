package pl.courses.online_courses_backend.other;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileStorageUtilImpl implements FileStorageUtil{

    @Override
    public void saveFile(MultipartFile multipartFile) throws IOException {

        String fileName = multipartFile.getOriginalFilename();
        //Save directory
        Path uploadPath = Paths.get("../Online_Courses/src/assets/course_icon");

        //create directory
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        multipartFile.transferTo(filePath);

    }

}
