package hello.matdil.domain.user.entity;

public enum UserStatus {
    ACTIVE,      // 정상 회원
    INACTIVE,    // 이메일 미인증
    WITHDRAWN,   // 회원 탈퇴 처리
    BANNED       // 관리자에 의해 차단된 회원
}