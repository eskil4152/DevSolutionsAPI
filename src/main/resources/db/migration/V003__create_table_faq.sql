CREATE TABLE faq
(
    id INTEGER PRIMARY KEY,
    question VARCHAR(255),
    answer VARCHAR(255)
);

CREATE sequence faq_id_generator;