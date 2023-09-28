package pl.courses.online_courses_backend.audit.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.courses.online_courses_backend.entity.UserEntity;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserEntity user) {
            return Optional.ofNullable(user.getUsername());
        }

        return Optional.empty();
    }
}
