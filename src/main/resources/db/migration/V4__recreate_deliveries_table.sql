DROP TABLE IF EXISTS deliveries;

CREATE TABLE deliveries
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id               BIGINT         NOT NULL,
    rider_id               BIGINT,
    address_city           VARCHAR(255)   NOT NULL,
    address_street         VARCHAR(255)   NOT NULL,
    address_detail_address VARCHAR(255)   NOT NULL,
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
