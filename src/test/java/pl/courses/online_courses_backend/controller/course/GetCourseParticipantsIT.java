package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.wrapper.ParticipantsDTO;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetCourseParticipantsIT extends BaseTest {

    private final String PATH = "/api/courses/get-course-participants";

    @Test
    void shouldReturnEmptyListOfParticipants() throws Exception {

        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString()));

        var result = asObject(response, ParticipantsDTO.class);

        //then:
        response.andExpect(status().isOk());
        assertEquals(0L, result.getParticipants().size());
    }

    @Test
    void shouldReturnListOfParticipants() throws Exception {

        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var userEntityTwo = TestFactory.UserEntityFactory.createUserEntity();
        userEntityTwo.setUsername("Test");

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        var courseUsersEntityTwo = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntityTwo)
                        .build())
                .owner(Boolean.FALSE)
                .participant(true)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .participantConfirmedAt(LocalDateTime.now())
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity, courseUsersEntityTwo));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntityTwo.setCourseUser(Sets.newHashSet(courseUsersEntityTwo));

        entityManager.persist(userEntity);
        entityManager.persist(userEntityTwo);
        entityManager.persist(courseEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString()));

        var result = asObject(response, ParticipantsDTO.class);

        //then:
        response.andExpect(status().isOk());
        assertAll(
                () -> assertEquals(1L, result.getParticipants().size()),
                () -> assertEquals(userEntityTwo.getId(), result.getParticipants().get(0).getUserId()),
                () -> assertEquals(userEntityTwo.getUsername(), result.getParticipants().get(0).getUsername()),
                () -> assertEquals(userEntityTwo.getPhoto(), result.getParticipants().get(0).getPhoto())
        );
    }

    @Test
    void shouldNotFindCourseForUserWhoDidNotCreateCourse() throws Exception {

        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var userEntityTwo = TestFactory.UserEntityFactory.createUserEntity();
        userEntityTwo.setUsername("Test");

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        var courseUsersEntityTwo = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntityTwo)
                        .build())
                .owner(Boolean.FALSE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity, courseUsersEntityTwo));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntityTwo.setCourseUser(Sets.newHashSet(courseUsersEntityTwo));

        entityManager.persist(userEntity);
        entityManager.persist(userEntityTwo);
        entityManager.persist(courseEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).with(user(userEntityTwo))
                .param("courseId", courseEntity.getId().toString()));

        //then:
        response.andExpect(status().isNotFound());
    }
}
