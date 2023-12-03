CREATE TABLE IF NOT EXISTS image
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    path         VARCHAR(255) NOT NULL
);

