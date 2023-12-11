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
import pl.courses.online_courses_backend.entity.TopicEntity;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.NoteDTO;
import pl.courses.online_courses_backend.model.TopicDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddTopicIT extends BaseTest {

    private final String PATH = "/api/courses/add-topic";

    @DisplayName("Should add topic for course")
    @Test
    void shouldAddTopicForCourse() throws Exception {

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

        var topicRequest = TopicDTO.builder()
                .title("Test topic")
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data("Test Data for topic").build())
                )
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(topicRequest)));

        //then:
        var result = asObject(request, TopicsDTO.class);

        request.andExpect(status().isCreated());
        assertEquals("Test topic", result.getTopics().get(0).getTitle());
        assertEquals("Test Data for topic", result.getTopics().get(0).getNotes().stream().toList().get(0).getData());
    }

    @DisplayName("Should throw error if course does not exist")
    @Test
    void shouldThrowErrorIfCourseDoesNotExist() throws Exception {

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

        var topicRequest = TopicDTO.builder()
                .title("Test topic")
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data("Test Data for topic").build())
                )
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .param("courseId", String.valueOf((courseEntity.getId() + 1L)))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(topicRequest)));

        //then:
        request.andExpect(status().isNotFound());
    }

    @DisplayName("Should throw error if course does not belong to user")
    @Test
    void shouldThrowErrorIfCourseDoesNotBelongToUser() throws Exception {

        //given:
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        var topicRequest = TopicDTO.builder()
                .title("Test topic")
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data("Test Data for topic").build())
                )
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(topicRequest)));

        //then:
        request.andExpect(status().isNotFound());
    }


    @ParameterizedTest
    @MethodSource("topicDataProvider")
    void shouldThrowBadRequestError(String title, String data, int status) throws Exception {
        //given:
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();
        courseEntity.setTopics(Sets.newHashSet(TopicEntity.builder().title("Test topic").build()));

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

        var topicRequest = TopicDTO.builder()
                .title(title)
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data(data).build())
                )
                .build();

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(topicRequest)));

        //then:
        request.andExpect(status().is(status));
    }

    public static Stream<Arguments> topicDataProvider() {
        return Stream.of(
                Arguments.of(null, null, 400),
                Arguments.of("Abcd", null, 400),
                Arguments.of("", null, 400),
                Arguments.of("Topic test", null, 400),
                Arguments.of("Topic test", "Abcd", 400),
                Arguments.of("Topic test", "", 400),
                Arguments.of("Test topic", "Data note in topic for course", 400)
        );
    }
}
