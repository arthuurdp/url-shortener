CREATE TABLE short_urls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_key VARCHAR(10) NULL UNIQUE,
    original_url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_clicked_at TIMESTAMP NULL,
    clicks INT NOT NULL DEFAULT 0,
    user_id BIGINT,
    CONSTRAINT fk_short_url_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);
