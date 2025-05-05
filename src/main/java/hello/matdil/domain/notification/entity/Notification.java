package hello.matdil.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "nofications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    private NotificationStatus status;

    @Builder
    public Notification(Long userId, String message, NotificationType type,
                        LocalDateTime scheduledAt, NotificationStatus status) {
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.isRead = false; // 기본값
        this.createdAt = LocalDateTime.now(); // 생성 시 자동 설정
    }

}