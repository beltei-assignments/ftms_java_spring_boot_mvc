-- Insert Users
INSERT INTO users (email,first_name,last_name,`password`) VALUES ('admin@example.com','Chetra','HONG','123');

-- Insert Businesses
INSERT INTO businesses (user_id, name) VALUES(1, 'FTMS Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'Chetra Solution IT Group Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'CC Computer Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'Croud Service Provider Cambodia Co,.Ltd');

-- Insert Balance
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Payrolls', 6999.99);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Savings', 12000.50);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Company expenses', 3000.88);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'For investments', 8000);
