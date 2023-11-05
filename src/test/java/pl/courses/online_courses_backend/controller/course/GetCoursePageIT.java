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
import pl.courses.online_courses_backend.exception.wrapper.ErrorList;
import pl.courses.online_courses_backend.model.wrapper.CoursesForListDTO;
import pl.courses.online_courses_backend.type.OrderType;
import pl.courses.online_courses_backend.type.SortType;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetCoursePageIT extends BaseTest {

    private final String PATH = "/api/courses/get-course-page";

    @DisplayName("Should return course list with 0 elements when Database Is Empty And Success Status")
    @Test
    void shouldReturnCourseListWith0ElementsWhenDatabaseIsEmptyAndSuccessStatus() throws Exception {

        //given:
        var paginationForCourseListDTORequest = TestFactory.PaginationForCourseListDTOFactory.createPaginationForCourseListDTO();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paginationForCourseListDTORequest)));

        //then:
        var result = asObject(response, CoursesForListDTO.class);

        response.andExpect(status().isOk());
        //and:
        assertThat(result.getCoursesForList(), empty());
    }

    @DisplayName("Should return course list with 1 elements when Database Is Filed And Success Status")
    @Test
    void shouldReturnCourseListWith1ElementsWhenDatabaseIsFiledAndSuccessStatus() throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();

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

        var paginationForCourseListDTORequest = TestFactory.PaginationForCourseListDTOFactory.createPaginationForCourseListDTO();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paginationForCourseListDTORequest)));

        //then:
        var result = asObject(response, CoursesForListDTO.class);

        response.andExpect(status().isOk());
        //and:
        assertEquals(result.getCoursesForList().size(), 1);
        assertAll(
                () -> assertEquals(result.getCoursesForList().get(0).getTitle(), courseEntity.getTitle()),
                () -> assertEquals(result.getCoursesForList().get(0).getTopic(), courseEntity.getTopic()),
                () -> assertEquals(result.getCoursesForList().get(0).getStartDate(), courseEntity.getStartDate()),
                () -> assertEquals(result.getCoursesForList().get(0).getDescription(), courseEntity.getDescription()),
                () -> assertEquals(result.getCoursesForList().get(0).getUsername(), userEntity.getUsername())
        );
    }

    @DisplayName("Should return course list with 0 elements when Database Is Filed And Success Status")
    @Test
    void shouldReturnCourseListWith0ElementsWhenDatabaseIsFiledAndSuccessStatus() throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();

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

        var paginationForCourseListDTORequest = TestFactory.PaginationForCourseListDTOFactory.createPaginationForCourseListDTOBuilder()
                .page(1)
                .order(OrderType.ASC)
                .sort(SortType.TITLE)
                .size(1)
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paginationForCourseListDTORequest)));

        //then:
        var result = asObject(response, CoursesForListDTO.class);

        response.andExpect(status().isOk());
        //and:
        assertEquals(result.getCoursesForList().size(), 0);
    }

    @ParameterizedTest
    @MethodSource("orderAndSortDataProvider")
    void shouldReturnListWith2ElementsWhenDatabaseIsFiledAndElementsAreSorted(OrderType orderType, SortType sortType, String value) throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        var courseEntityTwo = TestFactory.CourseEntityFactory.createCourseEntityBuilder()
                .title(value)
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

        var paginationForCourseListDTORequest = TestFactory.PaginationForCourseListDTOFactory.createPaginationForCourseListDTOBuilder()
                .page(0)
                .order(orderType)
                .sort(sortType)
                .size(2)
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paginationForCourseListDTORequest)));

        //then:
        var result = asObject(response, CoursesForListDTO.class);

        response.andExpect(status().isOk());
        //and:
        assertEquals(result.getCoursesForList().size(), 2);
        assertEquals(result.getCoursesForList().get(0).getTitle(), value);
    }

    private static Stream<Arguments> orderAndSortDataProvider() {
        return Stream.of(
                Arguments.of(OrderType.ASC, SortType.TITLE, "Course title"),
                Arguments.of(OrderType.DESC, SortType.TITLE, "Title course"),
                Arguments.of(OrderType.ASC, SortType.TOPIC, "Course title"),
                Arguments.of(OrderType.DESC, SortType.TOPIC, "Title course"),
                Arguments.of(OrderType.ASC, SortType.START_DATE, "Course title"),
                Arguments.of(OrderType.DESC, SortType.START_DATE, "Title course"),
                Arguments.of(OrderType.ASC, SortType.END_DATE, "Course title"),
                Arguments.of(OrderType.DESC, SortType.END_DATE, "Title course")
        );
    }

    @ParameterizedTest
    @MethodSource("paginationForCourseListDataProvider")
    void shouldReturnError(Integer page, OrderType orderType, SortType sortType, Integer size, String field) throws Exception {

        //given:
        var paginationForCourseListDTORequest = TestFactory.PaginationForCourseListDTOFactory.createPaginationForCourseListDTOBuilder()
                .page(page)
                .order(orderType)
                .sort(sortType)
                .size(size)
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paginationForCourseListDTORequest)));

        //then:
        var result = asObject(response, ErrorList.class);

        response.andExpect(status().isBadRequest());

        assertEquals(result.getErrorList().get(0).getField(), field);
    }

    private static Stream<Arguments> paginationForCourseListDataProvider() {
        return Stream.of(
                Arguments.of(null, OrderType.ASC, SortType.TITLE, 1, "page"),
                Arguments.of(1, null, SortType.TITLE, 1, "order"),
                Arguments.of(1, OrderType.ASC, null, 1, "sort"),
                Arguments.of(1, OrderType.ASC, SortType.TITLE, null, "size")
        );
    }
}