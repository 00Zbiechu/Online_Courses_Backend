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

    @Query(value = "SELECT c.title, c.start_date, c.end_date, c.topic, c.description, c.image, u.username " +
            "FROM courses c INNER JOIN courses_users cu ON c.id = cu.courses_id " +
            "INNER JOIN users u ON cu.users_id = u.id", nativeQuery = true)
    Page<CourseForList> findCoursesPage(Pageable pageable);

    @Query(value = "SELECT c.title, c.start_date, c.end_date, c.topic " +
            "FROM courses c INNER JOIN courses_users cu ON c.id = cu.courses_id " +
            "INNER JOIN users u ON cu.users_id = u.id where u.username = :username", nativeQuery = true)
    List<CourseForAdmin> getCourseDataForAdmin(String username);

}
