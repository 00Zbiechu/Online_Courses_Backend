package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.AddTopicDTO;
import pl.courses.online_courses_backend.model.FileDTO;
import pl.courses.online_courses_backend.model.NoteDTO;
import pl.courses.online_courses_backend.model.TopicDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetTopicIT extends BaseTest {

    private final String SAVE_PATH = "/api/courses/add-topic";
    private final String GET_PATH = "/api/courses/get-topics";

    @DisplayName("Should return empty topic list")
    @Test
    public void shouldReturnEmptyTopicList() throws Exception {

        //given:
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

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, GET_PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString()));

        //then:
        var result = asObject(request, TopicsDTO.class);

        request.andExpect(status().isOk());
        assertEquals(Lists.newArrayList(), result.getTopics());
    }

    @DisplayName("Should add and return topic list")
    @Test
    public void shouldAddAndReturnTopicList() throws Exception {

        //given:
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

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        var topicRequest = AddTopicDTO.builder()
                .title("Test topic")
                .note("Test Data for topic")
                .build();

        MockMultipartFile[] files = {
                new MockMultipartFile("addTopicDTO", null,
                        "application/json", asJson(topicRequest).getBytes()),

                new MockMultipartFile("files", "Test.jpg",
                        "image/jpg", asJson(topicRequest).getBytes())
        };

        //when
        var responseSave = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, SAVE_PATH)
                .file(files[0])
                .file(files[1])
                .param("courseId", courseEntity.getId().toString())
                .with(user(userEntity))
        );

        var resultSave = asObject(responseSave, TopicsDTO.class);

        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, GET_PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString()));

        //then
        var expectedResult = TopicsDTO.builder().topics(
                List.of(
                        TopicDTO.builder().id(resultSave.getTopics().get(0).getId()).title("Test topic")
                                .notes(
                                        List.of(
                                                NoteDTO.builder()
                                                        .id(resultSave.getTopics().get(0).getNotes().stream().toList().get(0).getId())
                                                        .data("Test Data for topic").build()
                                        )
                                )
                                .files(
                                        List.of(
                                                FileDTO.builder()
                                                        .id(resultSave.getTopics().get(0).getFiles().stream().toList().get(0).getId())
                                                        .name("Test.jpg")
                                                        .type("image/jpg")
                                                        .build()
                                        )
                                )
                                .build())
        ).build();

        var result = asObject(response, TopicsDTO.class);
        assertEquals(objectMapper.writeValueAsString(expectedResult), objectMapper.writeValueAsString(result));
    }

    @DisplayName("Should add and return topic list with password")
    @Test
    public void shouldAddAndReturnTopicListWithPassword() throws Exception {

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
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        var topicRequest = AddTopicDTO.builder()
                .title("Test topic")
                .note("Test Data for topic")
                .build();

        MockMultipartFile[] files = {
                new MockMultipartFile("addTopicDTO", null,
                        "application/json", asJson(topicRequest).getBytes()),

                new MockMultipartFile("files", "Test.jpg",
                        "image/jpg", asJson(topicRequest).getBytes())
        };

        //when
        var responseSave = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, SAVE_PATH)
                .file(files[0])
                .file(files[1])
                .param("courseId", courseEntity.getId().toString())
                .with(user(userEntity))
        );

        var resultSave = asObject(responseSave, TopicsDTO.class);

        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, GET_PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .param("password", "PasswordTest"));

        //then
        var expectedResult = TopicsDTO.builder().topics(
                List.of(
                        TopicDTO.builder().id(resultSave.getTopics().get(0).getId()).title("Test topic")
                                .notes(
                                        List.of(
                                                NoteDTO.builder()
                                                        .id(resultSave.getTopics().get(0).getNotes().stream().toList().get(0).getId())
                                                        .data("Test Data for topic").build()
                                        )
                                )
                                .files(
                                        List.of(
                                                FileDTO.builder()
                                                        .id(resultSave.getTopics().get(0).getFiles().stream().toList().get(0).getId())
                                                        .name("Test.jpg")
                                                        .type("image/jpg")
                                                        .build()
                                        )
                                )
                                .build())
        ).build();

        var result = asObject(response, TopicsDTO.class);
        assertEquals(objectMapper.writeValueAsString(expectedResult), objectMapper.writeValueAsString(result));
    }

    @DisplayName("Should add and not return topic list with wrong password")
    @Test
    public void shouldAddAndNotReturnTopicListCauseWrongPassword() throws Exception {

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
                .token(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));
        userEntity.setCourseUser(Sets.newHashSet(courseUsersEntity));

        entityManager.persist(userEntity);
        entityManager.persist(courseEntity);

        var topicRequest = AddTopicDTO.builder()
                .title("Test topic")
                .note("Test Data for topic")
                .build();

        MockMultipartFile[] files = {
                new MockMultipartFile("addTopicDTO", null,
                        "application/json", asJson(topicRequest).getBytes()),

                new MockMultipartFile("files", "Test.jpg",
                        "image/jpg", asJson(topicRequest).getBytes())
        };

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, SAVE_PATH)
                .file(files[0])
                .file(files[1])
                .param("courseId", courseEntity.getId().toString())
                .param("password", "PasswordTest")
        );

        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, GET_PATH)
                .param("courseId", courseEntity.getId().toString())
                .param("password", "1234")
        );

        //then
        response.andExpect(status().isBadRequest());
    }
}
