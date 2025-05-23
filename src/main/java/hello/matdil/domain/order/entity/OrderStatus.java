package hello.matdil.domain.order.entity;

public enum OrderStatus {
    CREATED,      // 주문 생성됨
    ACCEPTED,     // 가게가 주문 수락
    COOKING,      // 조리 중
    READY,        // 픽업 준비 완료
    DELIVERING,   // 배달 중
    COMPLETED,    // 배달 완료
    CANCELED,     // 주문 취소
    DELETED       // 주문 삭제
}
