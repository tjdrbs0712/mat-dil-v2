-- 1. 기존 reviews 테이블 컬럼 수정 및 추가
ALTER TABLE reviews
    MODIFY comment VARCHAR(1000),
    MODIFY rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- 2. review_images 테이블 생성
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
