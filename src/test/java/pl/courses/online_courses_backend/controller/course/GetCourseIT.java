package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.CourseWithAuthorDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class GetCourseIT extends BaseTest {

    private final String PATH = "/api/courses/get-course";

    @DisplayName("Should return course data")
    @Test
    void shouldReturnCourseData() throws Exception {

        //given:
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
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).param("courseId", courseEntity.getId().toString()).with(user(userEntity)));

        //then
        var result = asObject(request, CourseWithAuthorDTO.class);

        request.andExpect(status().is(200));

        assertAll(() -> {
            assertEquals("Course title", result.getTitle());
            assertEquals("Course topic", result.getTopic());
            assertEquals("Course description", result.getDescription());
            assertEquals(LocalDate.now(), result.getStartDate());
            assertEquals(LocalDate.now(), result.getEndDate());
            assertEquals("TestUsername", result.getUsername());
            assertNull(result.getPhoto());
        });
    }

    @DisplayName("Should return course data with password")
    @Test
    void shouldReturnCourseDataWithPassword() throws Exception {

        //given:
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        courseEntity.setPassword(passwordEncoder.encode("PasswordTest"));

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
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("courseId", courseEntity.getId().toString())
                .param("password", "PasswordTest")
        );

        //then
        var result = asObject(request, CourseWithAuthorDTO.class);

        request.andExpect(status().is(200));

        assertAll(() -> {
            assertEquals("Course title", result.getTitle());
            assertEquals("Course topic", result.getTopic());
            assertEquals("Course description", result.getDescription());
            assertEquals(LocalDate.now(), result.getStartDate());
            assertEquals(LocalDate.now(), result.getEndDate());
            assertEquals("TestUsername", result.getUsername());
            assertNull(result.getPhoto());
        });
    }

    @DisplayName("Should not return course data cause lack of password and user")
    @Test
    void shouldNotReturnCourseDataCauseLackOfPasswordAndUser() throws Exception {

        //given:
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        courseEntity.setPassword(passwordEncoder.encode("PasswordTest"));

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
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("courseId", courseEntity.getId().toString())
        );

        //then
        request.andExpect(status().is(400));
    }

    @DisplayName("Should not return course data with wrong password")
    @Test
    void shouldNotReturnCourseDataWithWrongPassword() throws Exception {

        //given:
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        courseEntity.setPassword(passwordEncoder.encode("PasswordTest"));

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
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("courseId", courseEntity.getId().toString())
                .param("password", "1234")
        );

        //then
        request.andExpect(status().is(400));
    }

    @DisplayName("Should return error course not exist")
    @Test
    void shouldReturnErrorCourseNotExist() throws Exception {

        //given:
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
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).param("courseId", String.valueOf(courseEntity.getId() + 1L)));

        //then
        request.andExpect(status().isNotFound());
    }
}
