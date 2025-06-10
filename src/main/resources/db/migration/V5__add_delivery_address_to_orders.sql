ALTER TABLE orders
    ADD COLUMN address_city VARCHAR(255) NOT NULL,
    ADD COLUMN address_street VARCHAR(255) NOT NULL AFTER address_city,
    ADD COLUMN address_detail_address VARCHAR(255) NOT NULL AFTER address_street;