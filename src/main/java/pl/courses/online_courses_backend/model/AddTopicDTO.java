package pl.courses.online_courses_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.FIELD_REQUIRED;
import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.WRONG_FIELD_SIZE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTopicDTO {

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 5, message = WRONG_FIELD_SIZE)
    private String title;

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 10, max = 3000, message = WRONG_FIELD_SIZE)
    private String note;
}
