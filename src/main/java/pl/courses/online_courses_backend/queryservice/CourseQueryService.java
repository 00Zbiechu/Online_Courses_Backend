package pl.courses.online_courses_backend.queryservice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.entity.QCourseEntity;
import pl.courses.online_courses_backend.model.SearchForCourseDTO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseQueryService {

    private final JPAQueryFactory queryFactory;
    private final QCourseEntity course = QCourseEntity.courseEntity;

    public List<CourseEntity> searchForCourses(SearchForCourseDTO searchForCourseDTO) {
        BooleanExpression whereClause = course.isNotNull();

        if (searchForCourseDTO.getTitle() != null && !searchForCourseDTO.getTitle().isEmpty()) {
            whereClause = whereClause.and(course.title.containsIgnoreCase(searchForCourseDTO.getTitle()));
        }

        if (searchForCourseDTO.getStartDate() != null) {
            whereClause = whereClause.and(course.startDate.eq(searchForCourseDTO.getStartDate()));
        }

        if (searchForCourseDTO.getEndDate() != null) {
            whereClause = whereClause.and(course.endDate.eq(searchForCourseDTO.getEndDate()));
        }

        if (searchForCourseDTO.getTopic() != null && !searchForCourseDTO.getTopic().isEmpty()) {
            whereClause = whereClause.and(course.topic.equalsIgnoreCase(searchForCourseDTO.getTopic()));
        }

        if (searchForCourseDTO.getUsername() != null && !searchForCourseDTO.getUsername().isEmpty()) {
            whereClause = whereClause.and(findOwnerOfCourse(searchForCourseDTO.getUsername()));
        }

        return queryFactory.selectFrom(course)
                .where(whereClause)
                .fetch();
    }

    private BooleanExpression findOwnerOfCourse(String username) {
        return course.courseUser.any().owner.eq(true).and(course.courseUser.any().courseUsersPK.userEntity.username.containsIgnoreCase(username));
    }
}
