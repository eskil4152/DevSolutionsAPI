CREATE TABLE USERS (
    id INTEGER PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(20) CHECK (role IN ('OWNER', 'ADMIN', 'MODERATOR', 'USER'))
);

CREATE sequence user_id_generator;