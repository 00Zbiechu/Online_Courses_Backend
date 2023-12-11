package pl.courses.online_courses_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
