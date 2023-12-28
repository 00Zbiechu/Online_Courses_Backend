package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.entity.CourseUsersEntity;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.repository.CourseUsersRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseUsersServiceImpl implements CourseUsersService {

    private final CourseUsersRepository courseUsersRepository;

    @Override
    public String confirmParticipation(String token) {

        CourseUsersEntity courseUsersEntity = courseUsersRepository.findByToken(token)
                .orElseThrow(() -> new CustomErrorException("participationToken", ErrorCodes.TOKEN_ERROR, HttpStatus.NOT_FOUND));

        if (courseUsersEntity.getParticipantConfirmedAt() != null) {
            return "Token already used";
        }

        if (courseUsersEntity.getTokenExpiresAt().isAfter(LocalDateTime.now())) {
            return "Token expired";
        }

        courseUsersEntity.setParticipantConfirmedAt(LocalDateTime.now());
        courseUsersEntity.setParticipant(true);
        return "Participation confirmed";
    }
}
