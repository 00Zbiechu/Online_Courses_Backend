package pl.courses.online_courses_backend.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEditableEntityAudit extends BaseEntityAudit {

    @Column(nullable = false, updatable = false)
    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime modificationDate;
}
