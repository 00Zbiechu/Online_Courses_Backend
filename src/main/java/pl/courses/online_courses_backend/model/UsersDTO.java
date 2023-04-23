package pl.courses.online_courses_backend.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDTO {

    private Long id;

    private String username;

    private String email;

    private String password;


}