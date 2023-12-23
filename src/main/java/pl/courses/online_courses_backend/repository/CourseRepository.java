package pl.courses.online_courses_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.CourseEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    @Query("SELECT c FROM CourseEntity c WHERE c.endDate >= local date")
    Page<CourseEntity> findActiveCourses(Pageable pageable);

    @Query("SELECT c FROM CourseEntity c JOIN c.courseUser cu JOIN cu.courseUsersPK.userEntity u WHERE u.id=:userId AND cu.owner=true")
    List<CourseEntity> findCoursesCreatedByUser(Long userId);

    Optional<CourseEntity> findByTitleAndDeletedFalse(String title);
}
