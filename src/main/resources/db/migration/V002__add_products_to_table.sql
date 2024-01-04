INSERT INTO products (id, name, description, price) VALUES ((SELECT nextval('id_generator')), 'Product One', 'Product number one', 299);
INSERT INTO products (id, name, description, price) VALUES ((SELECT nextval('id_generator')), 'Product Two', 'Product number two', 499);
INSERT INTO products (id, name, description, price) VALUES ((SELECT nextval('id_generator')), 'Product Three', 'Product number three', 699);
INSERT INTO products (id, name, description, price) VALUES ((SELECT nextval('id_generator')), 'Product Four', 'Product number four', 999);
INSERT INTO products (id, name, description, price) VALUES ((SELECT nextval('id_generator')), 'Product Five', 'Product number five', 1999);