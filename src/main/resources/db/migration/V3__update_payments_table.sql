DROP TABLE IF EXISTS payments;

CREATE TABLE payments
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id     BIGINT         NOT NULL,
    payment_key  VARCHAR(255)   UNIQUE,
    method       VARCHAR(50)    NOT NULL,  -- ENUM: CARD, CASH, NAVER_PAY, KAKAO_PAY, TOSS_PAY
    amount       DECIMAL(10, 2) NOT NULL,
    status       VARCHAR(50)    NOT NULL,  -- ENUM: READY, COMPLETED, FAILED, CANCELLED 등
    paid_at      DATETIME,
    fail_reason  TEXT,
    created_at   DATETIME       NOT NULL,
    updated_at   DATETIME       NOT NULL
);
