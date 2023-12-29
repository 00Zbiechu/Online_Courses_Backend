package pl.courses.online_courses_backend.controller.confirmRegistration;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.courses.online_courses_backend.BaseTest;
import pl.courses.online_courses_backend.TestFactory;
import pl.courses.online_courses_backend.authentication.Role;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConfirmRegistrationIT extends BaseTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String LOGIN_PATH = "/api/users/authenticate";

    private final String PATH = "/api/tokens/confirm";

    @Test
    public void shouldThrowErrorIfAccountIsNotActivated() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntityBuilder()
                .username("TestUsername")
                .email("Test@java.com.pl")
                .password("UserPassword")
                .tokens(Sets.newHashSet())
                .role(Role.USER)
                .deleted(false)
                .build();

        var confirmationToken = TestFactory.ConfirmationTokenFactory.createConfirmationTokenBuilder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .userEntity(userEntity)
                .build();

        userEntity.setConfirmationTokenEntities(Sets.newHashSet(confirmationToken));

        var loginRequest = TestFactory.AuthenticationRequestDTOFactory.createAuthenticationRequestDTOBuilder()
                .username("TestUsername")
                .password("UserPassword")
                .build();

        entityManager.persist(userEntity);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, LOGIN_PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then
        response.andExpect(status().isNotFound());
    }

    @Test
    public void shouldThrowErrorIfTokenIsExpired() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntityBuilder()
                .username("TestUsername")
                .email("Test@java.com.pl")
                .password("UserPassword")
                .tokens(Sets.newHashSet())
                .role(Role.USER)
                .deleted(false)
                .build();

        var confirmationToken = TestFactory.ConfirmationTokenFactory.createConfirmationTokenBuilder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().minusSeconds(15))
                .createdAt(LocalDateTime.now())
                .userEntity(userEntity)
                .build();

        userEntity.setConfirmationTokenEntities(Sets.newHashSet(confirmationToken));

        entityManager.persist(userEntity);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("token", confirmationToken.getToken()));

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Token expired"));
    }

    @Test
    public void shouldThrowErrorIfTokenDoesNotExist() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntityBuilder()
                .username("TestUsername")
                .email("Test@java.com.pl")
                .password("UserPassword")
                .tokens(Sets.newHashSet())
                .role(Role.USER)
                .deleted(false)
                .build();

        entityManager.persist(userEntity);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("token", UUID.randomUUID().toString()));

        //then
        response.andExpect(status().isNotFound());
    }

    @Test
    public void shouldActivateAccount() throws Exception {

        //given
        var userEntity = TestFactory.UserEntityFactory.createUserEntityBuilder()
                .username("TestUsername")
                .email("Test@java.com.pl")
                .password(passwordEncoder.encode("UserPassword"))
                .tokens(Sets.newHashSet())
                .role(Role.USER)
                .deleted(false)
                .build();

        var confirmationToken = TestFactory.ConfirmationTokenFactory.createConfirmationTokenBuilder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .userEntity(userEntity)
                .build();

        userEntity.setConfirmationTokenEntities(Sets.newHashSet(confirmationToken));

        entityManager.persist(userEntity);

        var loginRequest = TestFactory.AuthenticationRequestDTOFactory.createAuthenticationRequestDTOBuilder()
                .username("TestUsername")
                .password("UserPassword")
                .build();

        //when
        var responseActivation = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("token", confirmationToken.getToken()));

        var responseLogin = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, LOGIN_PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then
        responseActivation.andExpect(status().isOk());
        responseLogin.andExpect(status().isOk());
    }
}
