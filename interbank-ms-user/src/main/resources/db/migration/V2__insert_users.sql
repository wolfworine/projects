INSERT INTO user (firstname, lastname, address, email, type_document, document, username, password, phone_number, role, created_date)
VALUES
    ('Juan', 'Perez', 'Av. Lima 123', 'juan.perez@email.com', 'DNI', '12345678', 'juanperez', '$2a$10$zlh4F8RjZbqD3EGixw/KZeKCExFfEFM7lHxEAfVWIc4vDrbFYPK.O', '999888777', 'USER', NOW()),
    ('Maria', 'Lopez', 'Calle Sol 456', 'maria.lopez@email.com', 'PASSPORT', 'A1234567', 'marialopez', '$2a$10$PqzE6Sy8ly.KCpMLjW8akOKCMONZkME4TQeOiMzZ3Hz3GcKnf3U7u', '988777666', 'USER', NOW()),
    ('Admin', 'Master', 'Av. Central 789', 'admin@email.com', 'DNI', '87654321', 'adminmaster', '$2a$10$wBR1UgpiHZOclSK8QvPqce.uzqYNN1U9k/6kJ6UL8rRDzoAWLpgMO', '977666555', 'ADMIN', NOW());

INSERT INTO account (user_id, account_number, banking_entity, phone_number, device_serial, daily_limit, operation_limit, currency, balance)
VALUES
    (1, '123456789012', 'Banco Nacional', '999888777', 'DEVICE123', 2000.00, 10000.00, 'PEN', 500.00),
    (2, '987654321098', 'Banco Global', '988777666', 'DEVICE456', 1500.00, 7000.00, 'USD', 1000.00),
    (3, '112233445566', 'Banco Internacional', '977666555', 'DEVICE789', 3000.00, 15000.00, 'EUR', 250.00);

INSERT INTO transaction (origin_account, origin_number, target_account, target_number, amount, transaction_type, status)
VALUES
    (1, '999888777', 2, '988777666', 150.00, 'TRANSFER', 'COMPLETED'),
    (2, '988777666', 3, '977666555', 200.00, 'DEPOSIT', 'COMPLETED'),
    (3, '977666555', 1, '999888777', 50.00, 'WITHDRAWAL', 'PENDING');

INSERT INTO payment_method (user_id, type, provider, account_number, expiry_date)
VALUES
    (1, 'CREDIT_CARD', 'VISA', '4111111111111111', '2026-12-31'),
    (2, 'BANK_ACCOUNT', 'Banco Global', '987654321098', NULL),
    (3, 'PAYPAL', 'Paypal Inc.', 'paypaluser@email.com', NULL);

INSERT INTO balance (account_id, balance)
VALUES (1, 1000.00),  -- Cuenta 1 con saldo inicial de 1000 PEN
       (2, 500.00);   -- Cuenta 2 con saldo inicial de 500 PEN

UPDATE balance
SET balance = balance - 150.00
WHERE account_id = 1;

UPDATE balance
SET balance = balance + 150.00
WHERE account_id = 2;