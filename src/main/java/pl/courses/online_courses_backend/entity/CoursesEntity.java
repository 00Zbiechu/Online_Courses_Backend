package pl.courses.online_courses_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "COURSES")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SequenceGenerator(name = "generator_seq",
        sequenceName = "courses_id_seq",
        allocationSize = 1
)
public class CoursesEntity extends BaseEntity {

    @Column(length = 30, nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startData;

    @Column(nullable = false)
    private LocalDate endData;

    @Column(length = 30, nullable = false)
    private String topic;

    @Column(length = 30, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String description;

    @Column(length = 20)
    private String image;

    @ManyToMany
    @JoinTable(
            name = "COURSES_USERS",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<UsersEntity> users;


}
