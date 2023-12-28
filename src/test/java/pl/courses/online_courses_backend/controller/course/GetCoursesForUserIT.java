package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.authentication.Role;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.wrapper.CoursesDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

class GetCoursesForUserIT extends BaseTest {

    private final String PATH = "/api/courses/get-courses-for-user";

    @DisplayName("Should return only 1 course created by user")
    @Test
    void shouldReturnOnlyCourseCreatedByUser() throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var courseEntityTwo = TestFactory.CourseEntityFactory.createCourseEntityBuilder()
                .title("Test")
                .topic("Topic course")
                .description("Description course")
                .startDate(LocalDate.now().plusDays(1L))
                .endDate(LocalDate.now().plusDays(1L))
                .password("CoursePassword")
                .build();

        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var userEntityTwo = TestFactory.UserEntityFactory.createUserEntityBuilder()
                .username("TestUsername2")
                .email("Test2@java.com.pl")
                .password("UserPassword")
                .role(Role.USER)
                .deleted(false)
                .build();

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
                        .courseEntity(courseEntityTwo)
                        .userEntity(userEntityTwo)
                        .build())
                .owner(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        courseEntityTwo.setCourseUser(Sets.newHashSet(courseUsersEntityTwo));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(userEntityTwo);
        entityManager.persist(courseEntity);
        entityManager.persist(courseEntityTwo);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).with(user(userEntity)));
        var result = asObject(request, CoursesDTO.class);

        //then:
        assertAll(
                () -> assertEquals(1L, result.getFoundCoursesList().size()),
                () -> assertEquals("Course title", result.getFoundCoursesList().get(0).getTitle())
        );
    }


    @DisplayName("Should return 0 course created by user")
    @Test
    void shouldReturn0CourseCreatedByUser() throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var userEntityTwo = TestFactory.UserEntityFactory.createUserEntityBuilder()
                .username("TestUsername2")
                .email("Test2@java.com.pl")
                .password("UserPassword")
                .role(Role.USER)
                .deleted(false)
                .courseUser(Sets.newHashSet())
                .build();

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
        entityManager.persist(userEntityTwo);
        entityManager.persist(courseEntity);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).with(user(userEntityTwo)));
        var result = asObject(request, CoursesDTO.class);

        //then:
        assertNull(result.getFoundCoursesList());
    }
}
