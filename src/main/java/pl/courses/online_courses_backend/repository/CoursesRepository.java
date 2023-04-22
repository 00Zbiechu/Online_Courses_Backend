package pl.courses.online_courses_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.CoursesEntity;

@Repository
public interface CoursesRepository extends JpaRepository<CoursesEntity, Long> {
}
