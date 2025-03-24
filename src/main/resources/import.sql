-- Insert Users
INSERT INTO users (email,first_name,last_name,`password`) VALUES ('admin@example.com','Chetra','HONG','$2a$10$T/7NmYaRUNsSpi8uiNfExeJOS1AN8GiIzWxPQVhQrIj2Oc5zM4hPC');
INSERT INTO users (email,first_name,last_name,`password`) VALUES ('dara@example.com','Dara','SOK','$2a$10$T/7NmYaRUNsSpi8uiNfExeJOS1AN8GiIzWxPQVhQrIj2Oc5zM4hPC');

-- Insert Businesses
INSERT INTO businesses (user_id, name) VALUES(1, 'FTMS Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'Chetra Solution IT Group Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'CC Computer Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'Croud Service Provider Cambodia Co,.Ltd');

-- Insert Balances
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Payrolls', 6999.99);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Savings', 12000.50);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Company expenses', 3000.88);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'For investments', 8000);

-- Insert Transactions
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_type, notes) VALUES(1, 1, 1, 150, 'Expense', 'Electronic City');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_type, notes) VALUES(1, 2, 1, 100, 'Expense', 'Electronic City');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_type, notes) VALUES(1, 3, 1, 130, 'Expense', 'Electronic City');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_type, notes) VALUES(1, 4, 1, 140, 'Expense', 'Electronic City');
