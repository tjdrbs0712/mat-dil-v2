package hello.matdil.auth.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseTimeEntity {

    @Id
    private Long userId; // 사용자 ID (고유)

    @Column(nullable = false, length = 512)
    private String token;

    public void updateToken(String newToken) {
        this.token = newToken;
    }
}
