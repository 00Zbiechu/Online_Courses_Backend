package pl.courses.online_courses_backend.projection;

import lombok.Value;

import java.time.LocalDate;

@Value
public class CourseForEdit {

    String title;

    LocalDate startDate;

    LocalDate endDate;

    String topic;

}
