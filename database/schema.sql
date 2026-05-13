-- Portfolio Management System Database Schema
-- MySQL Database

CREATE DATABASE IF NOT EXISTS portfolio_db;
USE portfolio_db;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS holdings;
DROP TABLE IF EXISTS stocks;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    balance DECIMAL(15,2) DEFAULT 100000.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stocks (
    stock_id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(10) UNIQUE NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    sector VARCHAR(50),
    current_price DECIMAL(10,2) NOT NULL,
    previous_close DECIMAL(10,2),
    market_cap VARCHAR(20)
);

CREATE TABLE holdings (
    holding_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    stock_id INT NOT NULL,
    quantity INT NOT NULL,
    avg_buy_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (stock_id) REFERENCES stocks(stock_id),
    UNIQUE KEY uk_user_stock (user_id, stock_id)
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    stock_id INT NOT NULL,
    transaction_type ENUM('BUY', 'SELL') NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (stock_id) REFERENCES stocks(stock_id)
);

-- Sample Stock Data
INSERT INTO stocks (symbol, company_name, sector, current_price, previous_close, market_cap) VALUES
('RELIANCE', 'Reliance Industries Ltd', 'Energy', 2845.50, 2820.30, '19.2T'),
('TCS', 'Tata Consultancy Services', 'IT', 3678.20, 3654.10, '13.4T'),
('INFY', 'Infosys Limited', 'IT', 1547.80, 1532.45, '6.5T'),
('HDFC', 'HDFC Bank Limited', 'Banking', 1689.90, 1675.20, '12.8T'),
('ICICI', 'ICICI Bank Limited', 'Banking', 1124.50, 1110.75, '7.9T'),
('SBI', 'State Bank of India', 'Banking', 789.30, 781.60, '7.0T'),
('WIPRO', 'Wipro Limited', 'IT', 523.45, 518.90, '2.7T'),
('ITC', 'ITC Limited', 'FMCG', 456.80, 452.10, '5.7T'),
('LT', 'Larsen & Toubro', 'Construction', 3567.25, 3534.50, '4.9T'),
('MARUTI', 'Maruti Suzuki India', 'Auto', 11245.60, 11189.25, '3.4T');

-- Sample User (password: admin123)
INSERT INTO users (username, password, email, full_name, phone, balance) VALUES
('admin', 'admin123', 'admin@pms.com', 'Administrator', '9999999999', 500000.00),
('john', 'john123', 'john@example.com', 'John Doe', '9876543210', 100000.00);
