package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.TopicDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteTopicIT extends BaseTest {

    private final String PATH = "/api/courses/delete-topic";

    @DisplayName("Should delete topic")
    @Test
    void shouldDeleteTopic() throws Exception {
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        var topicEntity = TestFactory.TopicEntityFactory.createTopicEntityBuilder()
                .courseEntity(courseEntity)
                .title("Test title")
                .build();

        var topicEntityTwo = TestFactory.TopicEntityFactory.createTopicEntityBuilder()
                .courseEntity(courseEntity)
                .title("Test title2")
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        courseEntity.setTopics(Sets.newHashSet(topicEntity, topicEntityTwo));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        var expectedResult = List.of(
                TopicDTO.builder()
                        .id(topicEntity.getId())
                        .title(topicEntity.getTitle())
                        .notes(null)
                        .files(null)
                        .build()
        );


        //when
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .param("topicId", topicEntityTwo.getId().toString())
        );

        //then
        var result = asObject(request, TopicsDTO.class);
        request.andExpect(status().isOk());
        Assertions.assertEquals(expectedResult.toString(), result.getTopics().toString());
    }

    @DisplayName("Should throw error when deleting topic with non-existing topicId")
    @Test
    void shouldThrowErrorCauseTopicId() throws Exception {
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        var topicEntity = TestFactory.TopicEntityFactory.createTopicEntityBuilder()
                .courseEntity(courseEntity)
                .title("Test title")
                .build();

        var topicEntityTwo = TestFactory.TopicEntityFactory.createTopicEntityBuilder()
                .courseEntity(courseEntity)
                .title("Test title2")
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        courseEntity.setTopics(Sets.newHashSet(topicEntity, topicEntityTwo));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        //when
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .param("topicId", String.valueOf(topicEntity.getId() + 10L))
        );

        //then
        request.andExpect(status().isNotFound());
    }

    @DisplayName("Should throw error when deleting topic with non-existing courseId")
    @Test
    void shouldThrowErrorCauseCourseId() throws Exception {
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        var courseEntity = TestFactory.CourseEntityFactory.createCourseEntity();

        var courseUsersEntity = TestFactory.CourseUsersEntityFactory.createCourseUsersEntityBuilder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build())
                .owner(Boolean.TRUE)
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        var topicEntity = TestFactory.TopicEntityFactory.createTopicEntityBuilder()
                .courseEntity(courseEntity)
                .title("Test title")
                .build();

        var topicEntityTwo = TestFactory.TopicEntityFactory.createTopicEntityBuilder()
                .courseEntity(courseEntity)
                .title("Test title2")
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        courseEntity.setTopics(Sets.newHashSet(topicEntity, topicEntityTwo));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        //when
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).with(user(userEntity))
                .param("courseId", String.valueOf(courseEntity.getId() + 1))
                .param("topicId", topicEntity.getId().toString())
        );

        //then
        request.andExpect(status().isNotFound());
    }
}
