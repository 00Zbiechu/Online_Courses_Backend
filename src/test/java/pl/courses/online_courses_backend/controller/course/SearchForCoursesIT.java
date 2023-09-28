package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.wrapper.CoursesForListDTO;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchForCoursesIT extends BaseTest {

    private final String PATH = "/api/courses/search-for-courses";

    @ParameterizedTest
    @MethodSource("searchForCourseDataProvider")
    void searchForCoursesTest(String title, String topic, LocalDate startDate, LocalDate endDate, String username, int resultSize) throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var courseEntityTwo = TestFactory.CourseEntityFactory.createCourseEntityBuilder()
                .title("Title course")
                .topic("Course topic")
                .description("Description course")
                .startDate(LocalDate.now().plusDays(1L))
                .endDate(LocalDate.now().plusDays(1L))
                .password("CoursePassword")
                .deleted(false)
                .build();

        var userEntity = TestFactory.UserEntityFactory.createUserEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .build();

        var courseUsersEntityTwo = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntityTwo)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        courseEntityTwo.setCourseUser(Sets.newHashSet(courseUsersEntityTwo));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);
        entityManager.persist(courseEntityTwo);


        var searchForCourseDTORequest = TestFactory.SearchForCourseDTOFactory.createSearchForCourseDTOBuilder()
                .title(title)
                .topic(topic)
                .startDate(startDate)
                .endDate(endDate)
                .username(username)
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(searchForCourseDTORequest)));

        var result = asObject(request, CoursesForListDTO.class);

        //then:
        assertEquals(resultSize, result.getCoursesForList().size());
    }

    public static Stream<Arguments> searchForCourseDataProvider() {
        return Stream.of(
                Arguments.of(null, null, null, null, null, 2),
                Arguments.of("Course title", null, null, null, null, 1),
                Arguments.of("course ti", null, null, null, null, 1),
                Arguments.of("Title cou", null, null, null, null, 1),
                Arguments.of("title co", null, null, null, null, 1),
                Arguments.of("course", null, null, null, null, 2),
                Arguments.of("test", null, null, null, null, 0),
                Arguments.of(null, "Course topic", null, null, null, 2),
                Arguments.of(null, "Test", null, null, null, 0),
                Arguments.of(null, "CoUrse ToPiC", null, null, null, 2),
                Arguments.of(null, null, LocalDate.now(), null, null, 1),
                Arguments.of(null, null, null, LocalDate.now(), null, 1),
                Arguments.of(null, null, LocalDate.now().plusDays(1), null, null, 1),
                Arguments.of(null, null, null, LocalDate.now().plusDays(1), null, 1),
                Arguments.of(null, null, LocalDate.now().minusDays(1), null, null, 0),
                Arguments.of(null, null, null, LocalDate.now().minusDays(1), null, 0),
                Arguments.of(null, null, null, null, "TestUsername", 2),
                Arguments.of(null, null, null, null, "testusername", 2),
                Arguments.of(null, null, null, null, "UsernameTest", 0),
                Arguments.of("Course title", "Course topic", LocalDate.now(), LocalDate.now(), "TestUsername", 1),
                Arguments.of("Title course", "Course topic", LocalDate.now().plusDays(1), LocalDate.now().plusDays(1), "TestUsername", 1)
        );
    }
}
