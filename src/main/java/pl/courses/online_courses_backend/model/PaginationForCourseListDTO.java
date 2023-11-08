package pl.courses.online_courses_backend.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.courses.online_courses_backend.type.OrderType;
import pl.courses.online_courses_backend.type.SortType;

import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.FIELD_REQUIRED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PaginationForCourseListDTO {

    @NotNull(message = FIELD_REQUIRED)
    private Integer page;

    @NotNull(message = FIELD_REQUIRED)
    private Integer size;

    @NotNull(message = FIELD_REQUIRED)
    private SortType sort;

    @NotNull(message = FIELD_REQUIRED)
    private OrderType order;
}
