CREATE TABLE usuario (
    id                  VARCHAR(36)  NOT NULL PRIMARY KEY,
    email               VARCHAR(256) NOT NULL UNIQUE,
    senha               VARCHAR(256) NOT NULL,
    role                VARCHAR(32)  NOT NULL DEFAULT 'USER',
    created_date        TIMESTAMP    NOT NULL,
    last_modified_date  TIMESTAMP    NOT NULL
);
