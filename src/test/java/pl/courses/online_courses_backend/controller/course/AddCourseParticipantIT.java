package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AddCourseParticipantIT extends BaseTest {

    private final String PATH = "/api/courses/add-course-participant";

    @DisplayName("Should add course participant")
    @Test
    void shouldAddCourseParticipant() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var userEntityTwo = TestFactory.UserEntityFactory.createUserEntity();
        userEntityTwo.setUsername("Test");

        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(userEntityTwo);
        entityManager.persist(courseEntity);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .param("username", userEntityTwo.getUsername())
        );

        //then:
        request.andExpect(status().is(200));
    }

    @DisplayName("Should throw error cause is illegal to add owner")
    @Test
    void shouldThrowErrorCauseIsIllegalToAddOwner() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var userEntityTwo = TestFactory.UserEntityFactory.createUserEntity();
        userEntityTwo.setUsername("Test");

        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(userEntityTwo);
        entityManager.persist(courseEntity);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .param("username", userEntity.getUsername())
        );

        //then:
        request.andExpect(status().is(400));
    }

    @DisplayName("Should throw error cause user does not exist")
    @Test
    void shouldThrowErrorCauseUserDoesNotExist() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .param("username", "Test")
        );

        //then:
        request.andExpect(status().is(404));
    }

    @DisplayName("Should throw error cause user is already participant")
    @Test
    void shouldThrowErrorCauseUserIsAlreadyParticipant() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var userEntityTwo = TestFactory.UserEntityFactory.createUserEntity();
        userEntityTwo.setUsername("Test");

        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .build();

        var courseUsersEntityTwo = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntityTwo)
                        .build())
                .owner(Boolean.FALSE)
                .participant(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .participantConfirmedAt(LocalDateTime.now())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity, courseUsersEntityTwo));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntityTwo.setCourseUser(Sets.newHashSet(courseUsersEntityTwo));

        entityManager.persist(userEntity);
        entityManager.persist(userEntityTwo);
        entityManager.persist(courseEntity);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .param("username", userEntityTwo.getUsername())
        );

        //then:
        request.andExpect(status().is(400));
    }
}
