package pl.courses.online_courses_backend.controller.course;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HowManyCoursesIT extends BaseTest {

    private final String PATH = "/api/courses/how-many-courses";


    @DisplayName("Should return 0 and status ok when how-many-courses get is successful")
    @Test
    void shouldReturn0AndStatusOkWhenHowManyCoursesGetIsSuccessful() throws Exception {

        //when:
        var result = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH));

        //then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0));
    }

    @DisplayName("Should return 1 and status ok when how-many-courses get is successful and course exist")
    @Test
    void shouldReturn1AndStatusOkWhenHowManyCoursesGetIsSuccessfulAndCourseExist() throws Exception {

        //given:
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        entityManager.persist(courseEntity);

        //when:
        var result = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH));

        //then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }
}