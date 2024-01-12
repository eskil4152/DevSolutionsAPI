CREATE TABLE Orders (
    id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES Users(id),
    product_id BIGINT REFERENCES Products(id),
    price DECIMAL,
    orderDate TIMESTAMP,
    notes VARCHAR(255),
    paymentMethod VARCHAR(255),
    billingAddress VARCHAR(255),

    paymentStatus VARCHAR(20) CHECK (paymentStatus IN ('AWAITING_PAYMENT', 'PAYMENT_RECEIVED', 'ERROR')),
    orderStatus VARCHAR(20) CHECK (orderStatus IN ('NOT_STARTED', 'STARTED', 'COMPLETED'))
);

CREATE sequence order_id_generator;