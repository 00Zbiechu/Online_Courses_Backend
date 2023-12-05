package pl.courses.online_courses_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.courses.online_courses_backend.authentication.Role;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "APP_USER")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE APP_USER SET deleted = true, delete_date = now() WHERE id=?")
@Where(clause = "deleted=false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 60, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private byte[] photo;

    @Column(nullable = false)
    private boolean enabled = false;

    @OneToMany(mappedBy = "courseUsersPK.userEntity")
    private Set<CourseUsersEntity> courseUser = new HashSet<>();

    @OneToMany(
            mappedBy = "userEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private Set<TokenEntity> tokens = new HashSet<>();

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "userEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    private Set<ConfirmationTokenEntity> confirmationTokenEntities = new HashSet<>();

    @CreatedDate
    private LocalDateTime creationDate;

    @LastModifiedDate
    private LocalDateTime modificationDate;

    private boolean deleted = Boolean.FALSE;

    private LocalDateTime deleteDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
