package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EditCourseIT extends BaseTest {

    private final String PATH = "/api/courses/edit-course";

    @DisplayName("Should edit one course for logged user")
    @Test
    void shouldEditOneCourseForLoggedUser() throws Exception {

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

        var editCourseDTORequest = TestFactory.EditCourseDTOFactory.createEditCourseEdit(courseEntity.getId());

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(editCourseDTORequest)));

        request.andExpect(status().is(200));
    }


    @DisplayName("Should throw error if owner does not have course with specific id")
    @Test
    void shouldThrowErrorIfOwnerDoesNotHaveCourseWithSpecificId() throws Exception {

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

        var editCourseDTORequest = TestFactory.EditCourseDTOFactory.createEditCourseDTOBuilder()
                .id(courseEntity.getId() + 1L)
                .title("Course")
                .topic("Course topic")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .description("Course description")
                .password("Course_password")
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(editCourseDTORequest)));

        request.andExpect(status().is(404));
    }


    @DisplayName("Should throw error while editing course")
    @ParameterizedTest
    @MethodSource("editCourseDataProvider")
    void shouldThrowErrorWhileEditingCourseForLoggedUser(Long courseId, String title, String topic, LocalDate startDate, LocalDate endDate, String description, String password, Integer status) throws Exception {

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

        var editCourseDTORequest = TestFactory.EditCourseDTOFactory.createEditCourseDTOBuilder()
                .id(courseId)
                .title(title)
                .topic(topic)
                .startDate(startDate)
                .endDate(endDate)
                .description(description)
                .password(password)
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(editCourseDTORequest)));

        request.andExpect(status().is(status));
    }

    private static Stream<Arguments> editCourseDataProvider() {
        return Stream.of(
                Arguments.of(1L, "Course title", "Topic", LocalDate.now(), LocalDate.now(), "Course description", "Course_password", 400),
                Arguments.of(1L, null, "Topic", LocalDate.now(), LocalDate.now(), "Course description", "Course_password", 400),
                Arguments.of(1L, "Course title", null, LocalDate.now(), LocalDate.now(), "Course description", "Course_password", 400),
                Arguments.of(1L, "Course title", "Topic", null, LocalDate.now(), "Course description", "Course_password", 400),
                Arguments.of(1L, "Course title", "Topic", LocalDate.now(), null, "Course description", "Course_password", 400),
                Arguments.of(1L, "Course title", "Topic", LocalDate.now(), LocalDate.now(), null, "Course_password", 400),
                Arguments.of(1L, "Course title", "Topic", LocalDate.now(), LocalDate.now(), "Course description", null, 400),
                Arguments.of(1L, null, null, null, null, null, null, 400)
        );
    }
}
