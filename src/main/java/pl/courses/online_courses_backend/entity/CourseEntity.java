package pl.courses.online_courses_backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.courses.online_courses_backend.audit.BaseEditableEntityAudit;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COURSE")
@SQLDelete(sql = "UPDATE COURSE SET deleted = true, delete_date = now() WHERE id=?")
@Where(clause = "deleted=false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseEntity extends BaseEditableEntityAudit {

    @Column(length = 30, nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(length = 30, nullable = false)
    private String topic;

    @Column(length = 60)
    private String password;

    @Column(length = 50, nullable = false)
    private String description;

    private byte[] photo;

    @OneToMany(
            mappedBy = "courseUsersPK.courseEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private Set<CourseUsersEntity> courseUser = new HashSet<>();

    @OneToMany(
            mappedBy = "courseEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    private Set<TopicEntity> topics = new HashSet<>();
}
