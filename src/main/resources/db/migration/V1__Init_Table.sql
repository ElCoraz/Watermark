CREATE TABLE users
(
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN NOT NULL
) ENGINE = InnoDb;

CREATE TABLE authorities
(
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username),
    UNIQUE INDEX authorities_idx_1 (username, authority)
) ENGINE = InnoDb;

CREATE TABLE images
(
    id  VARCHAR(50) NOT NULL,
    url VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL
) ENGINE = InnoDb;

CREATE TABLE template
(
    id  VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    top VARCHAR(255) NOT NULL,
    bottom VARCHAR(255) NOT NULL
) ENGINE = InnoDb;

CREATE TABLE watermark
(
    id  VARCHAR(50) NOT NULL,
    url VARCHAR(255) NOT NULL,
    uuid VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    template VARCHAR(255) NOT NULL
) ENGINE = InnoDb;
