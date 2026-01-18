-- H2-compatible schema for tests

DROP TABLE IF EXISTS "comment";
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS item_request;
DROP TABLE IF EXISTS "user";

CREATE TABLE IF NOT EXISTS "user"
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE,

    CONSTRAINT chk_user_name CHECK (LENGTH(name) >= 1)
);

CREATE TABLE IF NOT EXISTS item_request
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    description  TEXT      NOT NULL,
    requester_id INT       NOT NULL,
    created      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_request_requester
        FOREIGN KEY (requester_id)
            REFERENCES "user" (id)
            ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS item
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT         NOT NULL,
    is_available BOOLEAN      NOT NULL DEFAULT TRUE,
    owner_id     INT          NOT NULL,
    request_id   INT,

    CONSTRAINT fk_item_owner
        FOREIGN KEY (owner_id)
            REFERENCES "user" (id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_item_request
        FOREIGN KEY (request_id)
            REFERENCES item_request (id)
            ON DELETE SET NULL,

    CONSTRAINT chk_item_name CHECK (LENGTH(name) >= 1)
);

CREATE TABLE IF NOT EXISTS booking
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    start     TIMESTAMP   NOT NULL,
    "end"     TIMESTAMP   NOT NULL,
    item_id   INT         NOT NULL,
    booker_id INT         NOT NULL,
    status    VARCHAR(50) NOT NULL,

    CONSTRAINT fk_booking_item
        FOREIGN KEY (item_id)
            REFERENCES item (id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_booking_booker
        FOREIGN KEY (booker_id)
            REFERENCES "user" (id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_booking_dates
        CHECK ("end" > start),
    CONSTRAINT chk_booking_status
        CHECK (status IN ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED'))
);

CREATE TABLE IF NOT EXISTS "comment"
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    author_id INT       NOT NULL,
    item_id   INT       NOT NULL,
    created   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    text      TEXT      NOT NULL,

    CONSTRAINT fk_comment_author
        FOREIGN KEY (author_id)
            REFERENCES "user" (id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_comment_item
        FOREIGN KEY (item_id)
            REFERENCES item (id)
            ON DELETE RESTRICT
);
