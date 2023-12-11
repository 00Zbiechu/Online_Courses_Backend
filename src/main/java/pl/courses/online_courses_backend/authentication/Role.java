package pl.courses.online_courses_backend.authentication;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static pl.courses.online_courses_backend.authentication.Permission.USER_CREATE;
import static pl.courses.online_courses_backend.authentication.Permission.USER_DELETE;
import static pl.courses.online_courses_backend.authentication.Permission.USER_READ;
import static pl.courses.online_courses_backend.authentication.Permission.USER_UPDATE;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER(
            Set.of(
                    USER_READ,
                    USER_DELETE,
                    USER_CREATE,
                    USER_UPDATE

            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = new java.util.ArrayList<>(getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
