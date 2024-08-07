-- 기존 테이블 삭제
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS venue;
DROP TABLE IF EXISTS event;

-- 새로운 테이블 생성
CREATE TABLE member
(
    member_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20)  NOT NULL,
    address      VARCHAR(255) NOT NULL
);

CREATE TABLE venue
(
    venue_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT          NOT NULL
);

CREATE TABLE event
(
    event_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    date     DATE         NOT NULL,
    time     TIME         NOT NULL,
    venue_id INT,
    FOREIGN KEY (venue_id) REFERENCES venue (venue_id)
);

CREATE TABLE ticket
(
    ticket_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id  BIGINT,
    price     DECIMAL(10, 2) NOT NULL,
    section   VARCHAR(50),
    row       VARCHAR(10),
    seat      VARCHAR(10),
    FOREIGN KEY (event_id) REFERENCES event (event_id)
);

CREATE TABLE reservation
(
    reservation_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id          BIGINT,
    ticket_id        BIGINT,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES member (member_id),
    FOREIGN KEY (ticket_id) REFERENCES ticket (ticket_id)
);
