package pl.courses.online_courses_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.ConfirmationTokenEntity;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {

    @Query("SELECT ct FROM ConfirmationTokenEntity ct JOIN ct.userEntity u WHERE ct.token=:token AND u.enabled=false")
    Optional<ConfirmationTokenEntity> findByToken(String token);
}
