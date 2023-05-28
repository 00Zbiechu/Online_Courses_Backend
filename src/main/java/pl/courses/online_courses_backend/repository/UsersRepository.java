package pl.courses.online_courses_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.UsersEntity;
import pl.courses.online_courses_backend.projection.UsernameAndEmailForEachToken;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    Optional<UsersEntity> findByUsername(String username);

    @Query("SELECT new pl.courses.online_courses_backend.projection.UsernameAndEmailForEachToken(u.username, u.email)" +
            " FROM UsersEntity u Join u.tokens t where t.token = :accessToken")
    Optional<UsernameAndEmailForEachToken> findUsersEntitiesByAccessToken(String accessToken);

}
