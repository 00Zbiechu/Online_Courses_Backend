package pl.courses.online_courses_backend.controller.confirmParticipation;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConfirmParticipationIT extends BaseTest {

    private final String PATH = "/api/course-users/confirm-participation";

    @Test
    void shouldThrowErrorCauseTokenIsExpired() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var token = UUID.randomUUID().toString();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.FALSE)
                .participant(Boolean.TRUE)
                .token(token)
                .tokenExpiresAt(LocalDateTime.now().minusDays(1))
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        //when
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("token", token)
        );

        //then
        request.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Token expired"));
    }

    @Test
    void shouldThrowErrorCauseTokenIsNotFound() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var token = UUID.randomUUID().toString();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.FALSE)
                .participant(Boolean.TRUE)
                .token(token)
                .tokenExpiresAt(LocalDateTime.now().minusDays(1))
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        //when
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("token", UUID.randomUUID().toString())
        );

        //then
        request.andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowErrorCauseTokenIsAlreadyUsed() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var token = UUID.randomUUID().toString();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.FALSE)
                .participant(Boolean.TRUE)
                .token(token)
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .participantConfirmedAt(LocalDateTime.now())
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        //when
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("token", token)
        );

        //then
        request.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Token already used"));
    }

    @Test
    void shouldConfirmParticipation() {

    }
}
