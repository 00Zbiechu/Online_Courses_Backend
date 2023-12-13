package pl.courses.online_courses_backend.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttachmentType {

    PDF("pdf"),
    JPEG("jpeg"),
    JPG("jpg"),
    PNG("png");

    private final String format;
}
