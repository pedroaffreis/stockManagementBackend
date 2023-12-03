--Password login will be root for both users.
INSERT INTO employee (id,address, email, first_name, last_name, password, personal_code, phone_number, role)
VALUES
(1,'example address', 'sam.simson@gmail.com', 'Samantha', 'Simpson', '$2a$10$EBqP6XZ6i0djqpUFY3HzEuvCAIO4pf7HUYwBNjQzaaaKdopmZeJxu', 47012120222, '+372588888', 'ROLE_USER'),
(2,'example address', 'john.jonson@gmail.com', 'John', 'Jonson', '$2a$10$HaIKS7V9PbZidD/CKmFtS.GGW36HoOXiGSfSh5WFovmmYKGdp51li', 37012120222, '+3725677777', 'ROLE_ADMIN');

INSERT INTO share (id, company_name, country, currency, economic_field, share_name, symbol)
VALUES
(1,'Apple Inc.', 'US', 'USD', 'Technology', 'Apple Inc', 'AAPL'),
(2,'American Assets Trust, Inc.', 'US', 'USD', 'Real Estate', 'American Assets Trust', 'AAT'),
(3,'Microsoft Corporation', 'US', 'USD', 'Technology', 'Microsoft', 'MSFT');

INSERT INTO transaction (id, trade_date, fx, purchase_price, transaction_type, trading_volume, employee_id, share_id)
VALUES
(1,'2023-11-04 02:00:00.000000', 1.25, 150.55, 'PURCHASE', 500, 2, 1),
(2,'2023-11-04 11:34:31.278000', 1.25, 350.55, 'PURCHASE', 50000, 2, 3),
(3,'2023-11-04 11:34:49.646000', 1.25, 375, 'SALE', 10000, 1, 3),
(4,'2023-11-04 11:35:21.438000', 1.25, 23.55, 'PURCHASE', 10000, 1, 2);