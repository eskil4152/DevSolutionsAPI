CREATE TABLE ACCOUNTS (
    id INTEGER PRIMARY KEY,
    username VARCHAR(20),
    email VARCHAR(40),
    password VARCHAR(255),
    role VARCHAR(20) CHECK (role IN ('OWNER', 'ADMIN', 'MODERATOR', 'USER'))
);

CREATE sequence user_id_generator;