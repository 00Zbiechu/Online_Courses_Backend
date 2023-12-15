package pl.courses.online_courses_backend.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.authentication.Role;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegisterIT extends BaseTest {

    private final String PATH = "/api/users/register";

    @DisplayName("Should register new user with photo")
    @Test
    void shouldRegisterNewUserWithPhoto() throws Exception {

        //given:
        var registrationRequest = TestFactory.UserDTOFactory.createUserDTO();

        var registrationJson = new MockMultipartFile(
                "userDTO",
                null,
                "application/json",
                asJson(registrationRequest).getBytes()
        );

        byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/image/test_image.jpg"));

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.png",
                "image/png", imageBytes);

        //when:
        var request = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(registrationJson)
                .file(photo)
        );

        //then:
        request.andExpect(status().isOk());
    }

    @DisplayName("Registration should throw error")
    @ParameterizedTest
    @MethodSource("registrationDataProvider")
    void registrationShouldThrowError(String username, String email, String password, Integer status) throws Exception {

        //given:
        var registrationRequest = TestFactory.UserDTOFactory.createUserDTOBuilder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        var registrationJson = new MockMultipartFile(
                "userDTO",
                null,
                "application/json",
                asJson(registrationRequest).getBytes()
        );

        var userEntity = TestFactory.UserEntityFactory.createUserEntityBuilder()
                .username("UserForTest")
                .email("TestTest@java.com.pl")
                .password("TestForApplicationPassword")
                .role(Role.USER)
                .deleted(false)
                .enabled(true)
                .build();

        entityManager.persist(userEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(registrationJson)
        );

        //then:
        response.andExpect(status().is(status));
    }

    private static Stream<Arguments> registrationDataProvider() {
        return Stream.of(
                Arguments.of("UserForTest", "TestTestTest@java.com.pl", "TestForApplicationPassword", 400),
                Arguments.of(null, "TestTestTest@java.com.pl", "TestForApplicationPassword", 400),
                Arguments.of("", "TestTestTest@java.com.pl", "TestForApplicationPassword", 400),
                Arguments.of("te", "TestTestTest@java.com.pl", "TestForApplicationPassword", 400),
                Arguments.of("TestTestTestTestTestTest", "TestTestTest@java.com.pl", "TestForApplicationPassword", 400),
                Arguments.of("UserForTestTest", null, "TestForApplicationPassword", 400),
                Arguments.of("UserForTestTest", "", "TestForApplicationPassword", 400),
                Arguments.of("UserForTestTest", "TestTest@java.com.pl", "TestForApplicationPassword", 400),
                Arguments.of("UserForTestTest", "TestTestTest", "TestForApplicationPassword", 400),
                Arguments.of("UserForTestTest", "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTest@java.com.pl", "TestForApplicationPassword", 400),
                Arguments.of("UserForTestTest", "@java.com", "TestForApplicationPassword", 400),
                Arguments.of("UserForTestTest", "@java.com", null, 400),
                Arguments.of("UserForTestTest", "@java.com", "", 400),
                Arguments.of("UserForTestTest", "@java.com", "Te", 400),
                Arguments.of("UserForTestTest", "@java.com", "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest", 400)
        );
    }
}
