INSERT INTO account (id, customer_id, country) VALUES
       (1, 1001, 'USA'),
       (2, 1002, 'Canada'),
       (3, 1003, 'UK');

INSERT INTO balance (id, account_id, available_amount, currency) VALUES
     (1, 1, 1000.0000, 'USD'),
     (2, 2, 2000.0000, 'CAD'),
     (3, 3, 1500.0000, 'GBP');

INSERT INTO transaction (id, account_id, amount, currency, direction, description, balance_after_transaction) VALUES
      (1, 1, 100.0000, 'EUR', 'IN', 'sissemakse', 1100.0000),
      (2, 2, 200.0000, 'CAD', 'OUT', 'automaat', 1800.0000),
      (3, 3, 150.0000, 'GBP', 'IN', 'testing', 1650.0000);

SELECT setval('transaction_id_seq', (SELECT MAX(id) FROM transaction));
