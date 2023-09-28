package pl.courses.online_courses_backend.entity;

import jakarta.persistence.*;
import lombok.*;
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
