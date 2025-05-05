package hello.matdil.domain.notification.entity;

public enum NotificationStatus {
    WAITING,    //아직 전송되지 않음(scheduleAt 이후 전송)
    SENDING,    //현재 전송중(비동기 큐 처리 중인 경우)
    SENT,       //전송 완료
    FAILED,     //전송 실패
    CANCELLED   //전송 취소
}