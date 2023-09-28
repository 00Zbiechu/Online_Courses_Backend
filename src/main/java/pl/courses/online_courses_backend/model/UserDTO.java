package pl.courses.online_courses_backend.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 3, max = 20, message = WRONG_FIELD_SIZE)
    private String username;

    @Email(message = WRONG_FIELD_FORMAT)
    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 3, max = 40, message = WRONG_FIELD_SIZE)
    private String email;

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 6, max = 30, message = WRONG_FIELD_SIZE)
    private String password;
}
