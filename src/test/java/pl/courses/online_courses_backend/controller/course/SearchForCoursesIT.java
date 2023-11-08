package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.exception.wrapper.ErrorList;
import pl.courses.online_courses_backend.type.OrderType;
import pl.courses.online_courses_backend.type.SortType;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .page(0)
                .order(OrderType.ASC)
                .sort(SortType.TITLE)
                .size(10)
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(searchForCourseDTORequest)));

        //then:
        request.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()").value(String.valueOf(resultSize)));
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


    @ParameterizedTest
    @MethodSource("sortSearchDataProvider")
    void shouldReturnSortedSearchResult(SortType sort, OrderType order, String title) throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var courseEntityTwo = TestFactory.CourseEntityFactory.createCourseEntityBuilder()
                .title("Title course")
                .topic("Topic course")
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
                .title("title")
                .topic(null)
                .startDate(null)
                .endDate(null)
                .username(null)
                .page(0)
                .order(order)
                .sort(sort)
                .size(2)
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(searchForCourseDTORequest)));

        //then:
        request.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(title));
    }

    private static Stream<Arguments> sortSearchDataProvider() {
        return Stream.of(
                Arguments.of(SortType.TITLE, OrderType.ASC, "Course title"),
                Arguments.of(SortType.TITLE, OrderType.DESC, "Title course"),
                Arguments.of(SortType.TOPIC, OrderType.ASC, "Course title"),
                Arguments.of(SortType.TOPIC, OrderType.DESC, "Title course"),
                Arguments.of(SortType.START_DATE, OrderType.ASC, "Course title"),
                Arguments.of(SortType.END_DATE, OrderType.DESC, "Title course")
        );
    }

    @ParameterizedTest
    @MethodSource("paginationDataProvider")
    void shouldReturnCourseListWithSpecificSizeDependsOnPagination(Integer page, Integer size, Integer foundCourses) throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var courseEntityTwo = TestFactory.CourseEntityFactory.createCourseEntityBuilder()
                .title("Title course")
                .topic("Topic course")
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
                .title("title")
                .topic(null)
                .startDate(null)
                .endDate(null)
                .username(null)
                .page(page)
                .order(OrderType.ASC)
                .sort(SortType.TITLE)
                .size(size)
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(searchForCourseDTORequest)));

        request.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()").value(foundCourses.toString()));
    }

    private static Stream<Arguments> paginationDataProvider() {
        return Stream.of(
                Arguments.of(0, 2, 2),
                Arguments.of(0, 1, 1),
                Arguments.of(1, 1, 1),
                Arguments.of(0, 4, 2),
                Arguments.of(1, 4, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("errorDataProvider")
    void shouldThrowError(Integer page, Integer size, OrderType orderType, SortType sortType, Integer errorSizeList, Integer status) throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var courseEntityTwo = TestFactory.CourseEntityFactory.createCourseEntityBuilder()
                .title("Title course")
                .topic("Topic course")
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
                .title("title")
                .topic(null)
                .startDate(null)
                .endDate(null)
                .username(null)
                .page(page)
                .order(orderType)
                .sort(sortType)
                .size(size)
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(searchForCourseDTORequest)));

        var result = asObject(request, ErrorList.class);

        //then:
        request.andExpect(status().is(status));

        assertEquals(errorSizeList, result.getErrorList().size());
    }

    private static Stream<Arguments> errorDataProvider() {
        return Stream.of(
                Arguments.of(null, null, null, null, 4, 400),
                Arguments.of(0, null, null, null, 3, 400),
                Arguments.of(0, 2, null, null, 2, 400),
                Arguments.of(0, 2, OrderType.ASC, null, 1, 400)
        );
    }
}
