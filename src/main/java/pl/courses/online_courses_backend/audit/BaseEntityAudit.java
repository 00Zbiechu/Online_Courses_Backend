package pl.courses.online_courses_backend.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.courses.online_courses_backend.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityAudit extends BaseEntity {

    @Column(nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime creationDate;

    @Column(nullable = false, updatable = false)
    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime modificationDate;
}
