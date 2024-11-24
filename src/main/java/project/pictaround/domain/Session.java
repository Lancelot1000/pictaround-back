package project.pictaround.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue
    @Column(name = "session_id")
    private Long id;

    private Long userId;

    private String sessionValue;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @Builder
    public Session(String sessionValue, Long userId, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.userId = userId;
        this.sessionValue = sessionValue;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
