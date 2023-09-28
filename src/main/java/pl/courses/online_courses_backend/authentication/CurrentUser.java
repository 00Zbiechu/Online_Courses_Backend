package pl.courses.online_courses_backend.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.courses.online_courses_backend.entity.UserEntity;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    public UserEntity getCurrentlyLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) auth.getPrincipal();
    }
}
