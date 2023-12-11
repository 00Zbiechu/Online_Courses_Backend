package pl.courses.online_courses_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.courses.online_courses_backend.authentication.TokenType;

import java.time.LocalDateTime;

@Entity
@Table(name = "TOKEN")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity extends BaseEntity {

    @Column(nullable = false, length = 300)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private UserEntity userEntity;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime creationDate;
}
