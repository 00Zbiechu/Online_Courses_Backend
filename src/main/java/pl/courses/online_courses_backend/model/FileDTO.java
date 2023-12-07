package pl.courses.online_courses_backend.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {

    private String name;

    private String type;

    private byte[] data;
}
