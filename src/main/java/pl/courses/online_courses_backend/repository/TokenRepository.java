package pl.courses.online_courses_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.courses.online_courses_backend.entity.TokenEntity;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Query("SELECT t FROM TokenEntity t join UsersEntity u" +
            " where u.id = :userId and (t.expired = false or t.revoked = false)")
    List<TokenEntity> findAllValidTokensByUser(Integer userId);


    Optional<TokenEntity> findByToken(String token);

}
