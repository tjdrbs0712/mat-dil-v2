package hello.matdil.mail.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "email_tokens")
public class EmailToken extends BaseTimeEntity {
    @Id
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public static EmailToken generate(String email) {
        return EmailToken.builder()
                        .token(UUID.randomUUID().toString())
                        .email(email)
                        .expiresAt(LocalDateTime.now().plusMinutes(30))
                        .build();
    }
}
