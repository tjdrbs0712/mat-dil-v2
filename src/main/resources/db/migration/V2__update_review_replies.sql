DROP TABLE IF EXISTS review_replies;

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
        FOREIGN KEY (review_id)REFERENCES reviews (id)
            ON DELETE CASCADE
);
