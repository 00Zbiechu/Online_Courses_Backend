package pl.courses.online_courses_backend.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.type.AttachmentExtensionType;
import pl.courses.online_courses_backend.type.AttachmentType;
import pl.courses.online_courses_backend.type.ImageType;

@Component
public class FileValidator {

    public void validatePhoto(MultipartFile photo) {
        validateIsNotEmpty(photo);
        validateImageFormat(photo);
    }

    private void validateIsNotEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateImageFormat(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !imageHasValidFormat(originalFilename)) {
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean imageHasValidFormat(String filename) {
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

    public void validateFile(MultipartFile file) {
        validateIsNotEmpty(file);
        validateFileFormat(file);
        validateFileType(file);
        validateFileName(file);
    }

    private void validateFileType(MultipartFile file) {
        if (!fileHasValidType(file.getContentType())) {
            throw new CustomErrorException("file", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean fileHasValidType(String fileType) {
        for (AttachmentType type : AttachmentType.values()) {
            if (fileType.equals(type.getType())) {
                return true;
            }
        }
        return false;
    }

    private void validateFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.length() > 40) {
            throw new CustomErrorException("file - name", ErrorCodes.WRONG_FIELD_SIZE, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateFileFormat(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !fileHasValidFormat(originalFilename)) {
            throw new CustomErrorException("file", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean fileHasValidFormat(String filename) {
        String[] parts = filename.split("\\.");
        if (parts.length > 1) {
            String fileExtension = parts[parts.length - 1].toLowerCase();
            for (AttachmentExtensionType attachmentExtensionType : AttachmentExtensionType.values()) {
                if (attachmentExtensionType.getFormat().equals(fileExtension)) {
                    return true;
                }
            }
        }
        return false;
    }
}
