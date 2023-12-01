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
    public void confirmAccount(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new CustomErrorException("confirmationToken", ErrorCodes.TOKEN_ERROR, HttpStatus.NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new CustomErrorException("confirmationToken", ErrorCodes.TOKEN_ALREADY_USED, HttpStatus.BAD_REQUEST);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new CustomErrorException("confirmationToken", ErrorCodes.TOKEN_EXPIRED, HttpStatus.BAD_REQUEST);
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        var user = confirmationToken.getUserEntity();
        user.setEnabled(true);
        confirmationTokenRepository.save(confirmationToken);
    }
}
