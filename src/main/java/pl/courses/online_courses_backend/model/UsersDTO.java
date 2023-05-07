package pl.courses.online_courses_backend.model;


import lombok.*;
import pl.courses.online_courses_backend.authentication.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDTO {


    private String username;

    private String email;

    private String password;

    private Role role;


}
