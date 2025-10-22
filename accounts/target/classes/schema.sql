CREATE TABLE IF NOT EXISTS `customer` (
                                          `customer_id` int AUTO_INCREMENT  PRIMARY KEY,
                                          `name` varchar(100) NOT NULL,
    `email` varchar(100) NOT NULL,
    `mobile_number` varchar(20) NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
    );

CREATE TABLE IF NOT EXISTS `accounts` (
                                          `customer_id` int NOT NULL,
                                          `account_number` int AUTO_INCREMENT  PRIMARY KEY,
                                          `account_type` varchar(100) NOT NULL,
    `branch_address` varchar(200) NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
    );

-- SQL SERVER
-- CREATE TABLE customer (
--                           customer_id INT IDENTITY(1,1) PRIMARY KEY,
--                           name VARCHAR(100) NOT NULL,
--                           email VARCHAR(100) NOT NULL,
--                           mobile_number VARCHAR(20) NOT NULL,
--                           created_at DATE NOT NULL,
--                           created_by VARCHAR(20) NOT NULL,
--                           updated_at DATE NULL,
--                           updated_by VARCHAR(20) NULL
-- );
--
-- CREATE TABLE accounts (
--                           account_number bigint PRIMARY KEY,
--                           customer_id INT NOT NULL,
--                           account_type VARCHAR(100) NOT NULL,
--                           branch_address VARCHAR(200) NOT NULL,
--                           created_at DATE NOT NULL,
--                           created_by VARCHAR(20) NOT NULL,
--                           updated_at DATE NULL,
--                           updated_by VARCHAR(20) NULL
-- );
