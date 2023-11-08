package pl.courses.online_courses_backend.queryservice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.entity.QCourseEntity;
import pl.courses.online_courses_backend.model.SearchForCourseDTO;
import pl.courses.online_courses_backend.type.OrderType;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseQueryService {

    private final JPAQueryFactory queryFactory;
    private final QCourseEntity course = QCourseEntity.courseEntity;

    public Page<CourseEntity> searchForCourses(SearchForCourseDTO searchForCourseDTO) {
        BooleanExpression whereClause = buildWhereClause(searchForCourseDTO);

        JPAQuery<CourseEntity> query = buildQuery(whereClause, searchForCourseDTO);

        List<CourseEntity> results = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(results, PageRequest.of(searchForCourseDTO.getPage(), searchForCourseDTO.getSize()), total);
    }

    private BooleanExpression buildWhereClause(SearchForCourseDTO searchForCourseDTO) {
        BooleanExpression whereClause = course.isNotNull();

        if (isNotBlank(searchForCourseDTO.getTitle())) {
            whereClause = whereClause.and(course.title.containsIgnoreCase(searchForCourseDTO.getTitle()));
        }

        if (searchForCourseDTO.getStartDate() != null) {
            whereClause = whereClause.and(course.startDate.eq(searchForCourseDTO.getStartDate()));
        }

        if (searchForCourseDTO.getEndDate() != null) {
            whereClause = whereClause.and(course.endDate.eq(searchForCourseDTO.getEndDate()));
        }

        if (isNotBlank(searchForCourseDTO.getTopic())) {
            whereClause = whereClause.and(course.topic.equalsIgnoreCase(searchForCourseDTO.getTopic()));
        }

        if (isNotBlank(searchForCourseDTO.getUsername())) {
            whereClause = whereClause.and(findOwnerOfCourse(searchForCourseDTO.getUsername()));
        }

        return whereClause;
    }

    private JPAQuery<CourseEntity> buildQuery(BooleanExpression whereClause, SearchForCourseDTO searchForCourseDTO) {
        JPAQuery<CourseEntity> query = queryFactory.selectFrom(course)
                .where(whereClause)
                .offset((long) searchForCourseDTO.getPage() * searchForCourseDTO.getSize())
                .limit(searchForCourseDTO.getSize());

        applySorting(query, searchForCourseDTO);

        return query;
    }

    private void applySorting(JPAQuery<CourseEntity> query, SearchForCourseDTO searchForCourseDTO) {
        switch (searchForCourseDTO.getSort()) {
            case TITLE:
                query.orderBy(searchForCourseDTO.getOrder() == OrderType.ASC ? course.title.asc() : course.title.desc());
                break;
            case START_DATE:
                query.orderBy(searchForCourseDTO.getOrder() == OrderType.ASC ? course.startDate.asc() : course.startDate.desc());
                break;
            case END_DATE:
                query.orderBy(searchForCourseDTO.getOrder() == OrderType.ASC ? course.endDate.asc() : course.endDate.desc());
                break;
            case TOPIC:
                query.orderBy(searchForCourseDTO.getOrder() == OrderType.ASC ? course.topic.asc() : course.topic.desc());
                break;
            default:
                break;
        }
    }

    private BooleanExpression findOwnerOfCourse(String username) {
        return course.courseUser.any().owner.eq(true).and(course.courseUser.any().courseUsersPK.userEntity.username.containsIgnoreCase(username));
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.isEmpty();
    }
}

