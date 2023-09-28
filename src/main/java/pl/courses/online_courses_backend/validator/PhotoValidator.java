package pl.courses.online_courses_backend.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.type.ImageType;

@Component
public class PhotoValidator {

    public void validate(MultipartFile photo) {
        validateIsImageIsNotEmpty(photo);
        validateImageFormat(photo);
    }

    private void validateIsImageIsNotEmpty(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FIELD_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateImageFormat(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !hasValidFormat(originalFilename)) {
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FIELD_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean hasValidFormat(String filename) {
        String[] parts = filename.split("\\.");
        if (parts.length > 1) {
            String fileExtension = parts[parts.length - 1].toLowerCase();
            for (ImageType imageType : ImageType.values()) {
                if (imageType.getFormat().equals(fileExtension)) {
                    return true;
                }
            }
        }
        return false;
    }
}
