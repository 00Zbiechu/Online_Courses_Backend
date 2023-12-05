package pl.courses.online_courses_backend.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.courses.online_courses_backend.entity.UserEntity;
import pl.courses.online_courses_backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class RegistrationScheduling {

    private final UserRepository userRepository;

    @Async
    @Scheduled(cron = "0 */30 * * * *")
    public void deleteNonActivatedUserAccount() {
        Set<UserEntity> usersToDelete = userRepository.findAll().stream()
                .filter(userEntity ->
                        userEntity.getConfirmationTokenEntities().stream()
                                .anyMatch(confirmationTokenEntity ->
                                        confirmationTokenEntity.getExpiresAt().isAfter(LocalDateTime.now())
                                                && confirmationTokenEntity.getConfirmedAt() == null))
                .collect(Collectors.toSet());

        userRepository.deleteAll(usersToDelete);
    }
}
