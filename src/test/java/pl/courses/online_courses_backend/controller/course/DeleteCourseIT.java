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

class DeleteCourseIT extends BaseTest {

    private final String PATH = "/api/courses/delete-course";

    @DisplayName("Should delete course if owner is logged")
    @Test
    void shouldDeleteCourseIfOwnerIsLogged() throws Exception {

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
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString()));

        //then:
        response.andExpect(status().isAccepted());
    }

    @Test
    void shouldThrowErrorIfOwnerDoesNotHaveCourseWithSpecificId() throws Exception {

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
        long courseId = courseEntity.getId() + 1;
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).with(user(userEntity))
                .param("courseId", Long.toString(courseId)));

        //then:
        response.andExpect(status().isNotFound());
    }
}
