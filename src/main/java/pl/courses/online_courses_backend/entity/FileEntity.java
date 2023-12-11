package pl.courses.online_courses_backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.courses.online_courses_backend.audit.BaseEntityAudit;

@Entity
@Table(name = "FILE")
@SQLDelete(sql = "UPDATE FILE SET deleted = true, delete_date = now() WHERE id=?")
@Where(clause = "deleted=false")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity extends BaseEntityAudit {

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 10)
    private String type;

    @Column(nullable = false)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "topic_id",
            referencedColumnName = "id"
    )
    private TopicEntity topicEntity;
}