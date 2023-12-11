package pl.courses.online_courses_backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.courses.online_courses_backend.audit.BaseEntityAudit;

import java.util.Set;

@Entity
@Table(name = "TOPIC")
@SQLDelete(sql = "UPDATE TOPIC SET deleted = true, delete_date = now() WHERE id=?")
@Where(clause = "deleted=false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicEntity extends BaseEntityAudit {

    @Column(nullable = false, length = 50)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_id",
            referencedColumnName = "id"
    )
    private CourseEntity courseEntity;


    @OneToMany(
            mappedBy = "topicEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    private Set<NoteEntity> notes;

    /*
    @OneToMany(
            mappedBy = "topicEntity",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    private Set<FileEntity> files;
     */
}
