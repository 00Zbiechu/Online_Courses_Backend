package pl.courses.online_courses_backend.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCoursesDTO {

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String title;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String topic;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String password;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 50)
    private String description;

    @NotEmpty
    @NotBlank
    private String image;

    @NotEmpty
    @NotBlank
    String username;

}
