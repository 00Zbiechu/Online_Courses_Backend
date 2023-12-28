package pl.courses.online_courses_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.CourseUsersEntity;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;

import java.util.Optional;

@Repository
public interface CourseUsersRepository extends JpaRepository<CourseUsersEntity, CourseUsersPK> {

    @Query("SELECT cu FROM CourseUsersEntity cu WHERE cu.token=:token")
    Optional<CourseUsersEntity> findByToken(String token);
}
