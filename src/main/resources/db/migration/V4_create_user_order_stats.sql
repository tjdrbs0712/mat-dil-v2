CREATE TABLE user_order_stats (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                  user_id BIGINT NOT NULL,
                                  store_id BIGINT NOT NULL,

                                  order_count INT NOT NULL DEFAULT 0,
                                  last_ordered_at DATETIME,

                                  INDEX idx_user_store (user_id, store_id)
);
