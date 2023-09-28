package pl.courses.online_courses_backend.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.FIELD_REQUIRED;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {

    @NotBlank(message = FIELD_REQUIRED)
    private String username;

    @NotBlank(message = FIELD_REQUIRED)
    private String password;
}
