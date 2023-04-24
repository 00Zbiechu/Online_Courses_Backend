package pl.courses.online_courses_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.CoursesEntity;

@Repository
public interface CoursesRepository extends JpaRepository<CoursesEntity, Long> {

    @Query("SELECT c FROM CoursesEntity c")
    Page<CoursesEntity> findCoursesPage(Pageable pageable);


}
