CREATE TABLE account (
                          id SERIAL PRIMARY KEY,
                          customer_id BIGINT NOT NULL,
                          country VARCHAR(255) NOT NULL
);

CREATE TABLE balance (
                          id SERIAL PRIMARY KEY,
                          account_id BIGINT NOT NULL,
                          available_amount DECIMAL(19, 4) NOT NULL,
                          currency VARCHAR(3) NOT NULL,
                          FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE transaction (
                              id SERIAL PRIMARY KEY,
                              account_id BIGINT NOT NULL,
                              amount DECIMAL(19, 4) NOT NULL,
                              currency VARCHAR(3) NOT NULL,
                              direction VARCHAR(3) CHECK (direction IN ('IN', 'OUT')),
                              description TEXT,
                              balance_after_transaction DECIMAL(19, 4),
                              FOREIGN KEY (account_id) REFERENCES account(id)
);
