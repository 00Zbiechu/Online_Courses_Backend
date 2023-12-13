package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.TopicEntity;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.AddTopicDTO;
import pl.courses.online_courses_backend.model.NoteDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AddTopicIT extends BaseTest {

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

        var topicRequest = AddTopicDTO.builder()
                .title("Test topic")
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data("Test Data for topic").build())
                )
                .build();

        MockMultipartFile topicJson = new MockMultipartFile("addTopicDTO", null,
                "application/json", asJson(topicRequest).getBytes());

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(topicJson)
                .param("courseId", courseEntity.getId().toString())
                .with(user(userEntity))
        );

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

        var topicRequest = AddTopicDTO.builder()
                .title("Test topic")
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data("Test Data for topic").build())
                )
                .build();

        MockMultipartFile topicJson = new MockMultipartFile("addTopicDTO", null,
                "application/json", asJson(topicRequest).getBytes());

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(topicJson)
                .param("courseId", String.valueOf((courseEntity.getId() + 1L)))
                .with(user(userEntity))
        );

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

        var topicRequest = AddTopicDTO.builder()
                .title("Test topic")
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data("Test Data for topic").build())
                )
                .build();

        MockMultipartFile topicJson = new MockMultipartFile("addTopicDTO", null,
                "application/json", asJson(topicRequest).getBytes());

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(topicJson)
                .param("courseId", courseEntity.getId().toString())
                .with(user(userEntity))
        );

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

        var topicRequest = AddTopicDTO.builder()
                .title(title)
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data(data).build())
                )
                .build();

        MockMultipartFile topicJson = new MockMultipartFile("addTopicDTO", null,
                "application/json", asJson(topicRequest).getBytes());

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(topicJson)
                .param("courseId", courseEntity.getId().toString())
                .with(user(userEntity)));

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

    @DisplayName("Should add topic for course with attachments")
    @ParameterizedTest
    @MethodSource("extensionDataProvider")
    void shouldAddTopicForCourseWithAttachments(String type, String extension) throws Exception {

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

        var topicRequest = AddTopicDTO.builder()
                .title("Test topic")
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data("Test Data for topic").build())
                )
                .build();

        MockMultipartFile[] files = {
                new MockMultipartFile("addTopicDTO", null,
                        "application/json", asJson(topicRequest).getBytes()),

                new MockMultipartFile("files", "Test." + extension,
                        type, asJson(topicRequest).getBytes())
        };


        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(files[0])
                .file(files[1])
                .param("courseId", courseEntity.getId().toString())
                .with(user(userEntity))
        );

        //then:
        var result = asObject(request, TopicsDTO.class);

        request.andExpect(status().isCreated());
        assertEquals("Test topic", result.getTopics().get(0).getTitle());
        assertEquals("Test Data for topic", result.getTopics().get(0).getNotes().stream().toList().get(0).getData());
        assertEquals("Test." + extension, result.getTopics().get(0).getFiles().stream().toList().get(0).getName());
        assertEquals(type, result.getTopics().get(0).getFiles().stream().toList().get(0).getType());
    }

    public static Stream<Arguments> extensionDataProvider() {
        return Stream.of(
                Arguments.of("image/jpeg", "jpeg"),
                Arguments.of("image/jpg", "jpg"),
                Arguments.of("image/png", "png"),
                Arguments.of("application/pdf", "pdf")
        );
    }

    @DisplayName("Should throw error while creating topic for course with attachments")
    @ParameterizedTest
    @MethodSource("attachmentsDataProvider")
    void shouldThrowErrorWhileCreatingTopicForCourseWithAttachments(String fileName, String type, byte[] data) throws Exception {

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

        var topicRequest = AddTopicDTO.builder()
                .title("Test topic")
                .notes(
                        Sets.newHashSet(NoteDTO.builder().data("Test Data for topic").build())
                )
                .build();

        MockMultipartFile[] files = {
                new MockMultipartFile("addTopicDTO", null,
                        "application/json", asJson(topicRequest).getBytes()),

                new MockMultipartFile("files", fileName,
                        type, data)
        };

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(files[0])
                .file(files[1])
                .param("courseId", courseEntity.getId().toString())
                .with(user(userEntity))
        );

        //then:
        request.andExpect(status().isBadRequest());
    }

    public static Stream<Arguments> attachmentsDataProvider() {
        return Stream.of(
                Arguments.of(null, null, null),
                Arguments.of("", "", RandomUtils.nextBytes(20)),
                Arguments.of(RandomStringUtils.random(41, true, true), "application/test", RandomUtils.nextBytes(20)),
                Arguments.of(RandomStringUtils.random(41, true, true), "application/jpeg", RandomUtils.nextBytes(20)),
                Arguments.of(".", "application/test", RandomUtils.nextBytes(20))
        );
    }
}
