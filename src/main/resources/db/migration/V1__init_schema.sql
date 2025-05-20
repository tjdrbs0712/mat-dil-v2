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
    phone_number           VARCHAR(255) NOT NULL,
    user_status            VARCHAR(50)  NOT NULL, -- ENUM: ACTIVE, INACTIVE, WITHDRAWN, BANNED
    withdrawn_at           DATETIME,
    last_login_at          DATETIME,
    created_at             DATETIME NOT NULL,
    updated_at             DATETIME NOT NULL,
    CONSTRAINT UK_user_email UNIQUE (email),
    CONSTRAINT UK_user_phone_number UNIQUE (phone_number)
);

-- ======================================
-- STORES TABLE
-- ======================================
CREATE TABLE stores
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    address_city           VARCHAR(255),
    address_street         VARCHAR(255),
    address_detail_address VARCHAR(255),
    name                   VARCHAR(255) NOT NULL,
    owner_id               BIGINT       NOT NULL,
    phone_number           VARCHAR(255) NOT NULL,
    open_time              TIME         NOT NULL,
    close_time             TIME         NOT NULL,
    status                 VARCHAR(50)  NOT NULL, -- ENUM: OPEN, CLOSED, INACTIVE
    min_order_price        INT          NOT NULL,
    delivery_time_estimate INT          NOT NULL,
    rating DOUBLE NOT NULL,
    review_count           INT          NOT NULL,
    created_at             DATETIME NOT NULL,
    updated_at             DATETIME NOT NULL
);

CREATE INDEX idx_store_rating_id ON stores (rating DESC, id DESC);
CREATE INDEX idx_store_review_id ON stores (review_count DESC, id DESC);
CREATE INDEX idx_store_delivery_id ON stores (delivery_time_estimate ASC, id ASC);
CREATE INDEX idx_store_name_id ON stores (name ASC, id ASC);

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
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,
    CONSTRAINT FK_menu_store FOREIGN KEY (store_id) REFERENCES stores (id) ON DELETE CASCADE
);

CREATE INDEX idx_menu_store_status_order ON menus (store_id, menu_status, order_index, id DESC);

-- ======================================
-- REVIEWS TABLE
-- ======================================
CREATE TABLE reviews
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    order_id   BIGINT NOT NULL,
    store_id   BIGINT NOT NULL,
    rating     INT    NOT NULL,
    comment    TEXT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- ======================================
-- REVIEW_REPLIES TABLE
-- ======================================
CREATE TABLE review_replies
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id  BIGINT NOT NULL,
    reply_text TEXT   NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- ======================================
-- PAYMENTS TABLE
-- ======================================
CREATE TABLE payments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT      NOT NULL,
    method     VARCHAR(50) NOT NULL, -- ENUM: CARD, CASH, NAVER_PAY, KAKAO_PAY, TOSS_PAY
    amount     INT         NOT NULL,
    is_paid    BOOLEAN     NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- ======================================
-- ORDERS TABLE
-- ======================================
CREATE TABLE orders
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                BIGINT      NOT NULL,
    store_id               BIGINT      NOT NULL,
    order_status           VARCHAR(50) NOT NULL, -- ENUM: CREATED, ACCEPTED, COOKING, READY, DELIVERING, COMPLETED, CANCELED
    total_price            INT         NOT NULL,
    request_note           VARCHAR(500),
    expected_delivery_time DATETIME    NOT NULL,
    is_paid                BOOLEAN     NOT NULL,
    created_at             DATETIME NOT NULL,
    updated_at             DATETIME NOT NULL
);

-- ======================================
-- ORDER_ITEMS TABLE (Entity-based)
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
    created_at   DATETIME NOT NULL,
    updated_at   DATETIME NOT NULL
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

-- ======================================
-- DELIVERIES TABLE
-- ======================================
CREATE TABLE deliveries
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id               BIGINT       NOT NULL,
    address_city           VARCHAR(255) NOT NULL,
    address_street         VARCHAR(255) NOT NULL,
    address_detail_address VARCHAR(255) NOT NULL,
    delivery_status        VARCHAR(50)  NOT NULL, -- ENUM: READY, PICKED_UP, IN_TRANSIT, DELIVERED, FAILED, CANCELED
    assigned_time          DATETIME NOT NULL,
    created_at             DATETIME NOT NULL,
    updated_at             DATETIME NOT NULL
);

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
    created_at      DATETIME NOT NULL,
    updated_at      DATETIME NOT NULL
);

-- ======================================
-- REFRESH TOKENS TABLE
-- ======================================
CREATE TABLE refresh_tokens
(
    user_id BIGINT PRIMARY KEY,
    token   VARCHAR(512) NOT NULL,
    created_at             DATETIME NOT NULL,
    updated_at             DATETIME NOT NULL
);

-- ======================================
-- EMAIL TOKENS TABLE
-- ======================================
CREATE TABLE email_tokens
(
    token      VARCHAR(255) PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    expires_at DATETIME     NOT NULL,
    created_at             DATETIME NOT NULL,
    updated_at             DATETIME NOT NULL
);