ALTER TABLE payments
    ADD COLUMN user_id BIGINT NOT NULL AFTER order_id;

CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_user_payment_status ON payments(user_id, payment_key, status);