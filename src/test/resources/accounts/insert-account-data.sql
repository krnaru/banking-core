INSERT INTO account (id, customer_id, country) VALUES
       (1, 1001, 'USA'),
       (2, 1002, 'Canada'),
       (3, 1003, 'UK');

INSERT INTO balance (id, account_id, available_amount, currency) VALUES
     (1, 1, 1000.0000, 'USD'),
     (2, 2, 2000.0000, 'CAD'),
     (3, 3, 1500.0000, 'GBP');

SELECT setval('account_id_seq', (SELECT MAX(id) FROM account));
SELECT setval('balance_id_seq', (SELECT MAX(id) FROM balance));
