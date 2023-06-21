package pl.courses.online_courses_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.UsersEntity;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    Optional<UsersEntity> findByUsername(String username);

    @Query(value = "SELECT u.username FROM users u INNER JOIN courses_users cu ON u.id = cu.users_id " +
            "INNER JOIN courses c ON cu.courses_id = c.id WHERE c.title=:title", nativeQuery = true)
    Optional<String> findUsernameByCourseTitle(String title);

}
