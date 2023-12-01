package pl.courses.online_courses_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;

import java.time.LocalDateTime;

@Entity
@Table(name = "COURSE_USERS")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE COURSE_USERS SET deleted = true, delete_date = now() WHERE course_id=? AND user_id=?")
@Where(clause = "deleted=false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseUsersEntity {

    @EmbeddedId
    private CourseUsersPK courseUsersPK;

    private boolean owner;

    @Column(nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime creationDate;

    private boolean deleted;

    private LocalDateTime deleteDate;
}
