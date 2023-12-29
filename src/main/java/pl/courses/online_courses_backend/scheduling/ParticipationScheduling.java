package pl.courses.online_courses_backend.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.courses.online_courses_backend.entity.CourseUsersEntity;
import pl.courses.online_courses_backend.repository.CourseUsersRepository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class ParticipationScheduling {

    private final CourseUsersRepository courseUsersRepository;

    @Async
    @Scheduled(cron = "0 */30 * * * *")
    public void deleteNonActivatedUserParticipation() {
        Set<CourseUsersEntity> participationToDelete = courseUsersRepository.findAll().stream()
                .filter(courseUsersEntity -> !courseUsersEntity.isOwner())
                .filter(courseUsersEntity -> !courseUsersEntity.isParticipant())
                .filter(courseUsersEntity -> courseUsersEntity.getParticipantConfirmedAt() == null)
                .filter(courseUsersEntity -> courseUsersEntity.getTokenExpiresAt().isAfter(LocalDateTime.now()))
                .collect(Collectors.toSet());

        courseUsersRepository.deleteAll(participationToDelete);
    }
}
