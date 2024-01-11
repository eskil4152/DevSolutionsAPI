CREATE TABLE UserOrders (
    id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES Users(id),
    order_id BIGINT REFERENCES Orders(id)
);

CREATE sequence user_order_id_generator;
