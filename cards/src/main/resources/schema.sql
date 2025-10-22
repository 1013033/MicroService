CREATE TABLE IF NOT EXISTS `cards` (
  `card_id` int NOT NULL AUTO_INCREMENT,
  `mobile_number` varchar(15) NOT NULL,
  `card_number` varchar(100) NOT NULL,
  `card_type` varchar(100) NOT NULL,
  `total_limit` int NOT NULL,
  `amount_used` int NOT NULL,
  `available_amount` int NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(20) NOT NULL,
  `updated_at` date DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`card_id`)
);

-- SQL SERVER
-- CREATE TABLE cards (
--                        card_id INT IDENTITY(1,1) PRIMARY KEY,
--                        mobile_number VARCHAR(15) NOT NULL,
--                        card_number VARCHAR(100) NOT NULL,
--                        card_type VARCHAR(100) NOT NULL,
--                        total_limit INT NOT NULL,
--                        amount_used INT NOT NULL,
--                        available_amount INT NOT NULL,
--                        created_at DATE NOT NULL,
--                        created_by VARCHAR(20) NOT NULL,
--                        updated_at DATE NULL,
--                        updated_by VARCHAR(20) NULL
-- );
