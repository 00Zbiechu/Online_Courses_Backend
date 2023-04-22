package pl.courses.online_courses_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "COURSES")
@Data
public class CoursesEntity extends BaseEntity {

    @Column(length = 20, nullable = false)
    private String username;

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

    @ManyToMany
    @JoinTable(
            name = "COURSES_USERS",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")

    )
    Set<UsersEntity> users;


}
