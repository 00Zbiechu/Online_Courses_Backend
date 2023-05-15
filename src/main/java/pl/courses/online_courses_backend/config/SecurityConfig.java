package pl.courses.online_courses_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import pl.courses.online_courses_backend.authentication.JwtAuthenticationFilter;

import static pl.courses.online_courses_backend.authentication.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()

                .requestMatchers("/api/courses/get-course-page**").permitAll()
                .requestMatchers("/api/courses/how-many-courses").permitAll()
                .requestMatchers("/api/courses/search-for-courses").permitAll()
                .requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/api/users/authenticate").permitAll()
                .requestMatchers("/api/users/logout").permitAll()

                .requestMatchers("/api/courses/get-course-data-for-calendar").hasRole(USER.name())
                .requestMatchers("/api/courses/get-course-data-for-edit").hasRole(USER.name())
                .requestMatchers("/api/courses/add-course").hasRole(USER.name())
                .requestMatchers("/api/courses/upload-file").hasRole(USER.name())

                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/users/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                });

        return http.build();

    }

}
