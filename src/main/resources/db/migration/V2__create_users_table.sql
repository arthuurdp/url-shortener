CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

ALTER TABLE short_urls ADD COLUMN user_id BIGINT;
ALTER TABLE short_urls ADD CONSTRAINT fk_short_url_user FOREIGN KEY (user_id) REFERENCES users(id);
