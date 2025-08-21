-- V2__create_products_table.sql
CREATE TABLE IF NOT EXISTS products
(
    product_id    BIGSERIAL PRIMARY KEY,
    product_name  VARCHAR(255) UNIQUE,
    description   TEXT,
    image         VARCHAR(255),
    quantity      INTEGER,
    price         DOUBLE PRECISION,
    discount      DOUBLE PRECISION,
    special_price DOUBLE PRECISION,
    category_id   BIGINT NOT NULL,
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES categories (category_id)
            ON DELETE CASCADE
);

-- Function to calculate special_price
CREATE OR REPLACE FUNCTION update_special_price()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.special_price := NEW.price - ((NEW.discount * 0.01) * NEW.price);
    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

-- Trigger to update special_price after insert or update
CREATE TRIGGER set_special_price
    BEFORE INSERT OR UPDATE
    ON products
    FOR EACH ROW
EXECUTE FUNCTION update_special_price();