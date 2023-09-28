package pl.courses.online_courses_backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        var jwtToken = authHeader.substring(7);

        var storedToken = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new CustomErrorException("token", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        if (storedToken != null) {
            tokenRepository.delete(storedToken);
        }
    }
}
