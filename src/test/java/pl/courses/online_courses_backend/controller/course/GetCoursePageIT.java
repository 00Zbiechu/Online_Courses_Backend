package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

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
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()").value("0"));
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
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
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
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(courseEntity.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].topic").value(courseEntity.getTopic()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].startDate").value(courseEntity.getStartDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value(courseEntity.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].username").value(userEntity.getUsername())
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
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
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
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()").value("0"));
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
                .build();

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

        var courseUsersEntityTwo = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntityTwo)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
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
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(value));
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