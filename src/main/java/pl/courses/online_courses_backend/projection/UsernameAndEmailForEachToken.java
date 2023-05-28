package pl.courses.online_courses_backend.projection;

import lombok.Value;

@Value
public class UsernameAndEmailForEachToken {

    String username;
    String email;

}
