package pl.courses.online_courses_backend.controller.course;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.model.AddTopicDTO;
import pl.courses.online_courses_backend.model.FileDataDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetAttachmentIT extends BaseTest {

    private final String PATH = "/api/courses/get-attachment";
    private final String SAVE_PATH = "/api/courses/add-topic";

    @DisplayName("Should return attachment")
    @Test
    public void shouldReturnAttachment() throws Exception {
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
                .note("Test Data for topic")
                .build();

        MockMultipartFile[] files = {
                new MockMultipartFile("addTopicDTO", null,
                        "application/json", asJson(topicRequest).getBytes()),

                new MockMultipartFile("files", "Test.jpg",
                        "image/jpg", asJson(topicRequest).getBytes())
        };

        //when:
        var saveRequest = mockMvc.perform(MockMvcRequestBuilders.multipart(SAVE_PATH)
                .file(files[0])
                .file(files[1])
                .param("courseId", courseEntity.getId().toString())
                .with(user(userEntity))
        );

        var saveResult = asObject(saveRequest, TopicsDTO.class);

        var request = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).with(user(userEntity))
                .param("courseId", courseEntity.getId().toString())
                .param("topicId", saveResult.getTopics().get(0).getId().toString())
                .param("fileId", saveResult.getTopics().get(0).getFiles().get(0).getId().toString())
        );

        var result = asObject(request, FileDataDTO.class);

        request.andExpect(status().isOk());
        Assertions.assertNotNull(result.getData());
    }
}
