package pl.courses.online_courses_backend.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttachmentType {

    PDF("application/pdf"),
    JPEG("image/jpeg"),
    JPG("image/jpg"),
    PNG("image/png");

    private final String type;
}
