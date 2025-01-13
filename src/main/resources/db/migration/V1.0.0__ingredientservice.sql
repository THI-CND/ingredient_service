CREATE TABLE ingredient_entity (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(255) NOT NULL
);

CREATE TABLE ingredient_entity_tags (
    ingredient_entity_id BIGINT       NOT NULL,
    tags                 VARCHAR(255) NOT NULL,
    CONSTRAINT fk_ingredient_entity
        FOREIGN KEY (ingredient_entity_id)
            REFERENCES ingredient_entity (id)
            ON DELETE CASCADE
);