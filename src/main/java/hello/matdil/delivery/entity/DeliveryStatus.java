package hello.matdil.delivery.entity;

public enum DeliveryStatus {
    READY,          // 배달 준비됨
    PICKED_UP,      // 배달원이 가게에서 픽업함
    IN_TRANSIT,     // 배달 중
    DELIVERED,      // 배달 완료
    FAILED,         // 배달 실패 (예: 고객 부재)
    CANCELED        // 배달 취소
}