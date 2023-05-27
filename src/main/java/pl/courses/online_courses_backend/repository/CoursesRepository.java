package pl.courses.online_courses_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.CoursesEntity;
import pl.courses.online_courses_backend.projection.CourseForAdmin;
import pl.courses.online_courses_backend.projection.CourseForList;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<CoursesEntity, Long>, JpaSpecificationExecutor<CoursesEntity> {

    @Query("SELECT new pl.courses.online_courses_backend.projection.CourseForList" +
            "(c.title, c.startDate, c.endDate, c.topic, c.description, c.image) FROM CoursesEntity c")
    Page<CourseForList> findCoursesPage(Pageable pageable);

    @Query("SELECT new pl.courses.online_courses_backend.projection.CourseForAdmin" +
            "(c.title, c.startDate, c.endDate, c.topic) FROM CoursesEntity c")
    List<CourseForAdmin> getCourseDataForAdmin();

}
