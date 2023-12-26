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
import pl.courses.online_courses_backend.model.CourseDTO;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AddCourseIT extends BaseTest {

    private final String PATH = "/api/courses/add-course";

    @DisplayName("Should add one course for logged user")
    @Test
    void shouldAddOneCourseForLoggedUser() throws Exception {

        //given:
        var addCourseDtoRequest = TestFactory.AddCourseDTOFactory.createAddCourseDTO();
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();

        entityManager.persist(userEntity);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(addCourseDtoRequest)));

        var result = asObject(request, CourseDTO.class);

        //then:
        request.andExpect(status().isCreated());
        assertAll(
                () -> assertEquals("Course", result.getTitle()),
                () -> assertEquals("Course topic", result.getTopic()),
                () -> assertEquals(LocalDate.now(), result.getStartDate()),
                () -> assertEquals(LocalDate.now(), result.getEndDate()),
                () -> assertEquals("Course description", result.getDescription()),
                () -> assertNull(result.getPhoto())
        );
    }

    @ParameterizedTest
    @MethodSource("addCourseDataProvider")
    void shouldThrowErrorWhenAddCourseEndpointIsHit(String title, String topic, LocalDate startDate, LocalDate endDate, String description, String password, Integer status) throws Exception {

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

        var addCourseDtoRequest = TestFactory.AddCourseDTOFactory.createAddCourseDTOBuilder()
                .title(title)
                .topic(topic)
                .startDate(startDate)
                .endDate(endDate)
                .description(description)
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(addCourseDtoRequest)));

        //then:
        request.andExpect(status().is(status));
    }

    private static Stream<Arguments> addCourseDataProvider() {
        return Stream.of(
                Arguments.of("Course title", "Topic", LocalDate.now(), LocalDate.now(), "Course description", "Course_password", 400),
                Arguments.of(null, "Topic", LocalDate.now(), LocalDate.now(), "Course description", "Course_password", 400),
                Arguments.of("Course title", null, LocalDate.now(), LocalDate.now(), "Course description", "Course_password", 400),
                Arguments.of("Course title", "Topic", null, LocalDate.now(), "Course description", "Course_password", 400),
                Arguments.of("Course title", "Topic", LocalDate.now(), null, "Course description", "Course_password", 400),
                Arguments.of("Course title", "Topic", LocalDate.now(), LocalDate.now(), null, "Course_password", 400),
                Arguments.of("Course title", "Topic", LocalDate.now(), LocalDate.now(), "Course description", null, 400),
                Arguments.of(null, null, null, null, null, null, 400)
        );
    }
}
