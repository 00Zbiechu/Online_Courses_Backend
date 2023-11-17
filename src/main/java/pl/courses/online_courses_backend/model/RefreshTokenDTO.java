package pl.courses.online_courses_backend.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.FIELD_REQUIRED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDTO {

    @NotBlank(message = FIELD_REQUIRED)
    private String refreshToken;
}
