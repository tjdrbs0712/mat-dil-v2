-- ======================================
-- USERS TABLE
-- ======================================
CREATE TABLE users
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                   VARCHAR(255) NOT NULL,
    email                  VARCHAR(100) NOT NULL,
    password               VARCHAR(255) NOT NULL,
    role                   VARCHAR(50)  NOT NULL, -- ENUM: USER, OWNER, ADMIN
    address_city           VARCHAR(255) NOT NULL,
    address_street         VARCHAR(255) NOT NULL,
    address_detail_address VARCHAR(255) NOT NULL,
    address_latitude       DOUBLE       NOT NULL, -- 위도 정보 추가
    address_longitude      DOUBLE       NOT NULL, -- 경도 정보 추가
    phone_number           VARCHAR(255) NOT NULL,
    user_status            VARCHAR(50)  NOT NULL, -- ENUM: ACTIVE, INACTIVE, WITHDRAWN, BANNED
    withdrawn_at           DATETIME,
    last_login_at          DATETIME,
    created_at             DATETIME     NOT NULL,
    updated_at             DATETIME     NOT NULL,
    CONSTRAINT UK_user_email UNIQUE (email),
    CONSTRAINT UK_user_phone_number UNIQUE (phone_number)
);

-- ======================================
-- STORES TABLE
-- ======================================
CREATE TABLE stores
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                   VARCHAR(255) NOT NULL,
    owner_id               BIGINT       NOT NULL,
    address_city           VARCHAR(255),
    address_street         VARCHAR(255),
    address_detail_address VARCHAR(255),
    address_latitude       DOUBLE, -- 위도 정보 추가 (nullable)
    address_longitude      DOUBLE, -- 경도 정보 추가 (nullable)
    image_url              VARCHAR(500),
    phone_number           VARCHAR(255) NOT NULL,
    open_time              TIME         NOT NULL,
    close_time             TIME         NOT NULL,
    status                 VARCHAR(50)  NOT NULL, -- ENUM: OPEN, CLOSED, INACTIVE
    min_order_price        INT          NOT NULL,
    delivery_time_estimate INT          NOT NULL,
    rating                 DOUBLE       NOT NULL,
    review_count           INT          NOT NULL,
    created_at             DATETIME     NOT NULL,
    updated_at             DATETIME     NOT NULL
);

CREATE INDEX idx_store_rating_id ON stores (rating DESC, id DESC);
CREATE INDEX idx_store_review_id ON stores (review_count DESC, id DESC);
CREATE INDEX idx_store_delivery_id ON stores (delivery_time_estimate ASC, id ASC);
CREATE INDEX idx_store_name_id ON stores (name ASC, id ASC);

-- ======================================
-- ORDERS TABLE (V5 및 좌표 변경사항 통합)
-- ======================================
CREATE TABLE orders
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                BIGINT       NOT NULL,
    store_id               BIGINT       NOT NULL,
    address_city           VARCHAR(255) NOT NULL,
    address_street         VARCHAR(255) NOT NULL,
    address_detail_address VARCHAR(255) NOT NULL,
    address_latitude       DOUBLE       NOT NULL, -- 위도 정보 추가
    address_longitude      DOUBLE       NOT NULL, -- 경도 정보 추가
    order_status           VARCHAR(50)  NOT NULL, -- ENUM: CREATED, ACCEPTED, COOKING, READY, DELIVERING, COMPLETED, CANCELED
    total_price            INT          NOT NULL,
    request_note           VARCHAR(500),
    expected_delivery_time DATETIME     NOT NULL,
    created_at             DATETIME     NOT NULL,
    updated_at             DATETIME     NOT NULL
);

-- ======================================
-- REVIEWS TABLE
-- ======================================
CREATE TABLE reviews
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    order_id   BIGINT   NOT NULL,
    store_id   BIGINT   NOT NULL,
    rating     INT      NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment    VARCHAR(1000),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT UK_review_user_store UNIQUE (user_id, store_id)
);

-- ======================================
-- REVIEW_REPLIES TABLE (V2 변경사항 통합)
-- ======================================
CREATE TABLE review_replies
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id  BIGINT        NOT NULL UNIQUE,
    owner_id   BIGINT        NOT NULL,
    reply_text VARCHAR(1000) NOT NULL,
    is_deleted BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at DATETIME      NOT NULL,
    updated_at DATETIME      NOT NULL,
    CONSTRAINT fk_review_reply_review_id
        FOREIGN KEY (review_id) REFERENCES reviews (id)
            ON DELETE CASCADE
);

-- ======================================
-- PAYMENTS TABLE (V3 변경사항 통합)
-- ======================================
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

-- ======================================
-- DELIVERIES TABLE (V4 및 좌표 변경사항 통합)
-- ======================================
CREATE TABLE deliveries
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id               BIGINT         NOT NULL,
    rider_id               BIGINT,
    address_city           VARCHAR(255)   NOT NULL,
    address_street         VARCHAR(255)   NOT NULL,
    address_detail_address VARCHAR(255)   NOT NULL,
    address_latitude       DOUBLE         NOT NULL, -- 위도 정보 추가
    address_longitude      DOUBLE         NOT NULL, -- 경도 정보 추가
    delivery_fee           DECIMAL(10, 2) NOT NULL,
    delivery_status        VARCHAR(50)    NOT NULL,
    assigned_time          DATETIME,
    picked_up_at           DATETIME,
    delivered_at           DATETIME,
    created_at             DATETIME       NOT NULL,
    updated_at             DATETIME       NOT NULL,
    CONSTRAINT uk_delivery_order_id UNIQUE (order_id)
);

CREATE INDEX idx_delivery_rider_id ON deliveries (rider_id);
CREATE INDEX idx_delivery_status ON deliveries (delivery_status);

-- ======================================
-- MENUS TABLE
-- ======================================
CREATE TABLE menus
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id    BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    price       INT          NOT NULL,
    description VARCHAR(500),
    image_url   VARCHAR(500),
    order_index INT          NOT NULL,
    category    VARCHAR(100),          -- ENUM: MAIN, SIDE, DRINK
    menu_status VARCHAR(20)  NOT NULL, -- ENUM: AVAILABLE, SOLD_OUT, HIDDEN, DELETE
    created_at  DATETIME     NOT NULL,
    updated_at  DATETIME     NOT NULL,
    CONSTRAINT FK_menu_store FOREIGN KEY (store_id) REFERENCES stores (id) ON DELETE CASCADE
);

CREATE INDEX idx_menu_store_status_order ON menus (store_id, menu_status, order_index, id DESC);

-- ======================================
-- review_images TABLE
-- ======================================
CREATE TABLE review_images
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url  VARCHAR(255) NOT NULL,
    review_id  BIGINT       NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    CONSTRAINT FK_review_image_review
        FOREIGN KEY (review_id) REFERENCES reviews (id)
            ON DELETE CASCADE
);

-- ======================================
-- ORDER_ITEMS TABLE
-- ======================================
CREATE TABLE order_items
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_id  BIGINT NOT NULL,
    quantity INT    NOT NULL,
    price    INT    NOT NULL,
    CONSTRAINT FK_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

-- ======================================
-- user_order_stats TABLE
-- ======================================
CREATE TABLE user_order_stats
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    store_id        BIGINT NOT NULL,
    order_count     INT    NOT NULL DEFAULT 0,
    last_ordered_at DATETIME,
    INDEX idx_user_store (user_id, store_id)
);

-- ======================================
-- NOTIFICATIONS TABLE
-- ======================================
CREATE TABLE notifications
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT      NOT NULL,
    message      TEXT        NOT NULL,
    type         VARCHAR(50) NOT NULL, -- ENUM: ORDER_COMPLETE, COUPON_ISSUED, REVIEW_REQUEST
    is_read      BOOLEAN     NOT NULL,
    scheduled_at DATETIME,
    status       VARCHAR(50) NOT NULL, -- ENUM: WAITING, SENDING, SENT, FAILED, CANCELLED
    created_at   DATETIME    NOT NULL,
    updated_at   DATETIME    NOT NULL
);

-- ======================================
-- FAVORITES TABLE
-- ======================================
CREATE TABLE favorites
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    store_id   BIGINT   NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT UK_favorite_user_store UNIQUE (user_id, store_id)
);

CREATE INDEX idx_favorite_user_id ON favorites (user_id);
CREATE INDEX idx_favorite_store_id ON favorites (store_id);

-- ======================================
-- COUPONS TABLE
-- ======================================
CREATE TABLE coupons
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT       NOT NULL,
    code            VARCHAR(255) NOT NULL UNIQUE,
    discount_amount INT          NOT NULL,
    expires_at      DATETIME     NOT NULL,
    created_at      DATETIME     NOT NULL,
    updated_at      DATETIME     NOT NULL
);

-- ======================================
-- REFRESH TOKENS TABLE
-- ======================================
CREATE TABLE refresh_tokens
(
    user_id    BIGINT PRIMARY KEY,
    token      VARCHAR(512) NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL
);

-- ======================================
-- EMAIL TOKENS TABLE
-- ======================================
CREATE TABLE email_tokens
(
    token      VARCHAR(255) PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    expires_at DATETIME     NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL
);