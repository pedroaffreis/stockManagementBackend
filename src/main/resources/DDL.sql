CREATE TABLE `employee`(
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `address` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `personal_code` BIGINT NOT NULL UNIQUE,
    `phone_number` VARCHAR(255) NOT NULL,
    `role` ENUM('ROLE_USER', 'ROLE_ADMIN') NOT NULL
);
CREATE TABLE `share`(
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `company_name` VARCHAR(255) NOT NULL,
    `country` VARCHAR(255) NOT NULL,
    `currency` VARCHAR(255) NOT NULL,
    `economic_field` VARCHAR(255) NOT NULL,
    `share_name` VARCHAR(255) NOT NULL,
    `symbol` VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE `transaction`(
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `trade_date` DATETIME NOT NULL,
    `fx` DOUBLE(8, 2) NOT NULL,
    `purchase_price` DOUBLE(8, 2) NOT NULL,
    `transaction_type` ENUM('PURCHASE','SALE') NOT NULL,
    `trading_volume` BIGINT NOT NULL,
    `employee_id` BIGINT NOT NULL,
    `share_id` BIGINT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (share_id) REFERENCES share(id)
);
