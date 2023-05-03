package pl.courses.online_courses_backend.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursesDTO {

//    private Long id;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String title;

    @NotNull
    @FutureOrPresent
    private LocalDate startData;

    @NotNull
    @FutureOrPresent
    private LocalDate endData;

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
    @Size(min = 3, max = 20)
    private String image;

}
