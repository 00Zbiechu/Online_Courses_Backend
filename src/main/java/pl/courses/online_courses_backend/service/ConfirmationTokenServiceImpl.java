package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.entity.ConfirmationTokenEntity;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.repository.ConfirmationTokenRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public String confirmAccount(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new CustomErrorException("confirmationToken", ErrorCodes.TOKEN_ERROR, HttpStatus.NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            return "Token already used";
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        var user = confirmationToken.getUserEntity();
        user.setEnabled(true);
        confirmationTokenRepository.save(confirmationToken);
        return "Account confirmed";
    }
}
