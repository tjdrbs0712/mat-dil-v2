package hello.matdil.domain.user.entity;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.user.dto.UserInfoChangeRequestDto;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true,length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

    @Column
    private LocalDateTime withdrawnAt;

    @Column
    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public User(String name, String email, String password,
                UserRole role, Address address, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.userStatus = UserStatus.INACTIVE;
        this.createdAt = LocalDateTime.now();
        this.lastLoginAt = null;
    }

    public boolean isPasswordMatch(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.password);
    }

    public void update(UserInfoChangeRequestDto dto) {
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getCity() != null && dto.getStreet() != null && dto.getDetailAddress() != null) {
            this.address = new Address(dto.getCity(), dto.getStreet(), dto.getDetailAddress());
        }
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void userStatusWithdraw(){
        userStatus = UserStatus.WITHDRAWN;
        this.withdrawnAt = LocalDateTime.now();
    }

    public void validateLoginPossible() {
        if (this.userStatus == UserStatus.WITHDRAWN) {
            throw new UserException(UserErrorCode.WITHDRAWN_USER);
        }

        if (this.userStatus == UserStatus.BANNED) {
            throw new UserException(UserErrorCode.BANNED_USER);
        }
    }

    public void verifyEmail(){
        this.userStatus = UserStatus.ACTIVE;
    }
}