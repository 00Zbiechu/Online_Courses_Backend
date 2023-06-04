package pl.courses.online_courses_backend.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface CourseForList {

    String getTitle();

    @Value("#{target.start_date}")
    LocalDate getStartDate();

    @Value("#{target.end_date}")
    LocalDate getEndDate();

    String getTopic();

    String getDescription();

    String getImage();
    

    String getUsername();

}
