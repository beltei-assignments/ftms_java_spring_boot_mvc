-- Insert Users
INSERT INTO users (email,first_name,last_name,`password`) VALUES ('admin@example.com','Chetra','HONG','$2a$10$T/7NmYaRUNsSpi8uiNfExeJOS1AN8GiIzWxPQVhQrIj2Oc5zM4hPC');
INSERT INTO users (email,first_name,last_name,`password`) VALUES ('dara@example.com','Dara','SOK','$2a$10$T/7NmYaRUNsSpi8uiNfExeJOS1AN8GiIzWxPQVhQrIj2Oc5zM4hPC');

-- Insert Businesses
INSERT INTO businesses (user_id, name) VALUES(1, 'FTMS Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'Chetra Solution IT Group Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'CC Computer Co,.Ltd');
INSERT INTO businesses (user_id, name) VALUES(1, 'Croud Service Provider Cambodia Co,.Ltd');

-- Insert Balances
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Payrolls', 100);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Savings', 1000);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'Company expenses', 1000);
INSERT INTO balances (user_id, name, balance) VALUES(1, 'For investments', 400);

-- Insert Transactions
-- Expense
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 1, 4, 4000, 'office supplies', 'Expense', 'Rent office', '2025-06-01 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 1, 4, 4000, 'office supplies', 'Expense', 'Rent office', '2025-07-01 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 1, 4, 4000, 'office supplies', 'Expense', 'Rent office', '2025-08-01 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 1, 3, 150, 'office supplies', 'Expense', 'Electronic City', '2025-07-01 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 2, 4, 450, 'utilities', 'Expense', 'Investing to buy new PC', '2025-07-2 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 3, 3, 130, 'office supplies', 'Expense', 'Electronic City', '2025-07-01 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 4, 3, 140, 'office supplies', 'Expense', 'Electronic City', '2025-07-04 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 3, 3, 1500, 'marketing', 'Expense', 'Buy laptops', '2025-06-01 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 3, 3, 1500, 'marketing', 'Expense', 'Buy laptops', '2025-07-01 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 3, 3, 1500, 'marketing', 'Expense', 'Buy laptops', '2025-08-01 17:57:38');

-- Income
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 1, 1, 500, 'payroll', 'Income', 'Daily income', '2025-07-01 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 2, 1, 300, 'payroll', 'Income', 'Daily income', '2025-07-02 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 3, 1, 530, 'payroll', 'Income', 'Daily income', '2025-07-03 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 4, 1, 840, 'payroll', 'Income', 'Daily income', '2025--07-04 17:57:38');
INSERT INTO transactions (user_id, business_id, balance_id, amount, transaction_category, transaction_type, notes, created_at) VALUES(1, 4, 1, 840, 'payroll', 'Income', 'Daily income', '2025-07-05 17:57:38');
