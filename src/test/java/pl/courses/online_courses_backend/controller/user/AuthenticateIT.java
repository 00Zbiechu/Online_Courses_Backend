package pl.courses.online_courses_backend.controller.user;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.authentication.Role;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticateIT extends BaseTest {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String PATH = "/api/users/authenticate";

    @DisplayName("User should login into account")
    @Test
    void userShouldLoginIntoAccount() throws Exception {

        //given:
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        userEntity.setPassword(passwordEncoder.encode("UserPassword"));
        entityManager.persist(userEntity);

        var loginRequest = TestFactory.AuthenticationRequestDTOFactory.createAuthenticationRequestDTOBuilder()
                .username("TestUsername")
                .password("UserPassword")
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then:
        response.andExpect(status().isOk());
    }

    @DisplayName("User should not login into account")
    @ParameterizedTest
    @MethodSource("loginDataProvider")
    void userShouldNotLoginIntoAccount(String username, String password, Integer status) throws Exception {

        //given:
        var userEntity = TestFactory.UserEntityFactory.createUserEntity();
        userEntity.setPassword(passwordEncoder.encode("UserPassword"));
        entityManager.persist(userEntity);

        var loginRequest = TestFactory.AuthenticationRequestDTOFactory.createAuthenticationRequestDTOBuilder()
                .username(username)
                .password(password)
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then:
        response.andExpect(status().is(status));
    }

    @Test
    public void shouldThrowErrorIfAccountIsNotActivated() throws Exception {

        //given:
        var userEntity = TestFactory.UserEntityFactory.createUserEntityBuilder()
                .username("TestUsername")
                .email("Test@java.com.pl")
                .password(passwordEncoder.encode("UserPassword"))
                .tokens(Sets.newHashSet())
                .role(Role.USER)
                .deleted(false)
                .build();

        entityManager.persist(userEntity);

        var loginRequest = TestFactory.AuthenticationRequestDTOFactory.createAuthenticationRequestDTOBuilder()
                .username("TestUsername")
                .password("UserPassword")
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then:
        response.andExpect(status().isNotFound());
    }

    private static Stream<Arguments> loginDataProvider() {
        return Stream.of(
                Arguments.of("", "", 400),
                Arguments.of(null, null, 400),
                Arguments.of("One", "Two", 404),
                Arguments.of("TestUsername", "Test", 401)
        );
    }
}
