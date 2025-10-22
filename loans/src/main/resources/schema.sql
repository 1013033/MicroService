CREATE TABLE IF NOT EXISTS `loans` (
  `loan_id` int NOT NULL AUTO_INCREMENT,
  `mobile_number` varchar(15) NOT NULL,
  `loan_number` varchar(100) NOT NULL,
  `loan_type` varchar(100) NOT NULL,
  `total_loan` int NOT NULL,
  `amount_paid` int NOT NULL,
  `outstanding_amount` int NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(20) NOT NULL,
  `updated_at` date DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`loan_id`)
);

-- SQL SERVER
-- CREATE TABLE loans (
--                        loan_id INT IDENTITY(1,1) PRIMARY KEY,
--                        mobile_number VARCHAR(15) NOT NULL,
--                        loan_number VARCHAR(100) NOT NULL,
--                        loan_type VARCHAR(100) NOT NULL,
--                        total_loan INT NOT NULL,
--                        amount_paid INT NOT NULL,
--                        outstanding_amount INT NOT NULL,
--                        created_at DATE NOT NULL,
--                        created_by VARCHAR(20) NOT NULL,
--                        updated_at DATE NULL,
--                        updated_by VARCHAR(20) NULL
-- );
