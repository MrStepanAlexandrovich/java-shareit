CREATE TABLE IF NOT EXISTS "user"
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE,

    CONSTRAINT chk_user_name CHECK (LENGTH(name) >= 1),
    CONSTRAINT chk_user_email CHECK (email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$')
);

CREATE TABLE IF NOT EXISTS item_request
(
    id           SERIAL PRIMARY KEY,
    description  TEXT      NOT NULL,
    requester_id INT       NOT NULL,
    created      TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_request_requester
        FOREIGN KEY (requester_id)
            REFERENCES "user" (id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_request_description CHECK (LENGTH(description) >= 10)
);


CREATE TABLE IF NOT EXISTS item
(
    id           SERIAL PRIMARY KEY,
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

    CONSTRAINT chk_item_name CHECK (LENGTH(name) >= 1),
    CONSTRAINT chk_item_description CHECK (LENGTH(description) >= 10)
);

CREATE TABLE IF NOT EXISTS booking
(
    id        SERIAL PRIMARY KEY,
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

CREATE TABLE IF NOT EXISTS comment
(
    id        SERIAL PRIMARY KEY,
    author_id INT       NOT NULL,
    item_id   INT       NOT NULL,
    created   TIMESTAMP NOT NULL DEFAULT NOW(),
    text      TEXT      NOT NULL,


    CONSTRAINT fk_comment_author
        FOREIGN KEY (author_id)
            REFERENCES "user" (id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_comment_item
        FOREIGN KEY (item_id)
            REFERENCES item (id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_comment_text CHECK (LENGTH(text) >= 5)
);


