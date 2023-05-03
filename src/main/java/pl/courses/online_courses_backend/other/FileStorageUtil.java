package pl.courses.online_courses_backend.other;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageUtil {

     void saveFile(MultipartFile multipartFile) throws IOException;

}
