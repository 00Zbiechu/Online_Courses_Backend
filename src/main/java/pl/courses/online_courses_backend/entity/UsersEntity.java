package pl.courses.online_courses_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SequenceGenerator(name = "generator_seq",
        sequenceName = "users_id_seq",
        allocationSize = 1
)
public class UsersEntity extends BaseEntity {

    @Column(length = 20, nullable = false)
    private String username;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String password;

    @ManyToMany(mappedBy = "users")
    Set<CoursesEntity> courses;

}
