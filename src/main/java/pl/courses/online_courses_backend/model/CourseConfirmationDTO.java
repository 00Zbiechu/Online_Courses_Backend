package pl.courses.online_courses_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseConfirmationDTO {

    private String title;

    private String mail;

    private String confirmationLink;
}
