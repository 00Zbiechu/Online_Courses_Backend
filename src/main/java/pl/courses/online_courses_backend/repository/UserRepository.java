package pl.courses.online_courses_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u where u.username=:username AND u.enabled=true")
    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u where u.email=:email AND u.enabled=true")
    Optional<UserEntity> findByEmail(String email);
}
