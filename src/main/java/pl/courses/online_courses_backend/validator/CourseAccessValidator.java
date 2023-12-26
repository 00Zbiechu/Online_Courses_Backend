package pl.courses.online_courses_backend.validator;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.courses.online_courses_backend.authentication.CurrentUser;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;

@Component
public class CourseAccessValidator {

    public void validateCourseAccess(CourseEntity courseEntity, CurrentUser currentUser, PasswordEncoder passwordEncoder, String password) {

        if (!validateUserIsParticipant(courseEntity, currentUser) && !validatePasswordIsCorrectForCourse(courseEntity, passwordEncoder, password)) {
            throw new CustomErrorException("course", ErrorCodes.NO_ACCESS, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validateUserIsParticipant(CourseEntity courseEntity, CurrentUser currentUser) {
        String username;
        try {
            username = currentUser.getCurrentlyLoggedUser().getUsername();
        } catch (CustomErrorException ex) {
            return false;
        }

        return courseEntity.getCourseUser().stream()
                .anyMatch(userWithAccess ->
                        userWithAccess.getCourseUsersPK()
                                .getUserEntity()
                                .getUsername().equals(username));
    }

    private boolean validatePasswordIsCorrectForCourse(CourseEntity courseEntity, PasswordEncoder passwordEncoder, String password) {
        if (validatePasswordForCourseIsSet(courseEntity) && (password == null || password.isBlank())) {
            throw new CustomErrorException("password", ErrorCodes.FIELD_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (!validatePasswordForCourseIsSet(courseEntity)) {
            return true;
        }

        return passwordEncoder.matches(password, courseEntity.getPassword());
    }

    private boolean validatePasswordForCourseIsSet(CourseEntity courseEntity) {
        return courseEntity.getPassword() != null && !courseEntity.getPassword().isBlank();
    }
}
