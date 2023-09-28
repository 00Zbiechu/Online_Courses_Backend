package pl.courses.online_courses_backend.mapper.decorator;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.entity.CourseUsersEntity;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.mapper.CourseMapper;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.CourseForListDTO;

@NoArgsConstructor
public abstract class CourseMapperDecorator implements CourseMapper {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CourseEntity toEntity(AddCourseDTO addCourseDTO) {
        addCourseDTO.setPassword(passwordEncoder.encode(addCourseDTO.getPassword()));
        return courseMapper.toEntity(addCourseDTO);
    }

    @Override
    public CourseForListDTO toCourseForList(CourseEntity courseEntity) {

        String username = courseEntity.getCourseUser()
                .stream()
                .filter(CourseUsersEntity::isOwner)
                .findFirst()
                .map(courseUser -> courseUser.getCourseUsersPK().getUserEntity().getUsername())
                .orElseThrow(() -> new CustomErrorException("username", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        var result = courseMapper.toCourseForList(courseEntity);

        result.setUsername(username);

        return result;
    }
}
