CREATE TABLE products
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    price DECIMAL
);

CREATE sequence id_generator;