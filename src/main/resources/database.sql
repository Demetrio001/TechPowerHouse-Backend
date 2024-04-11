
INSERT INTO card (id, name, creator, category, price, quantity, version)
VALUES
    (1, 'Card1', 'Creator1', 'Category1', 10.99, 5, 1),
    (2, 'Card2', 'Creator2', 'Category2', 15.50, 3, 1),
    (3, 'Card3', 'Creator3', 'Category3', 20.75, 8, 1);

INSERT INTO carts (id, user_id)
VALUES
    (1, 1), -- Cart 1, associato all'utente con ID 1
    (2, 2); -- Cart 2, associato all'utente con ID 2

INSERT INTO cart_details (id, card, price, quantity, cart, subtotal)
VALUES
    (5, 1, 20.50, 2, 1, 41.00),   -- Dettaglio del carrello 1: due carte con ID 1 a 20.50 ciascuna
    (4, 2, 15.75, 1, 1, 15.75);  -- Dettaglio del carrello 1: una carta con ID 2 a 15.75


INSERT INTO orders (id, create_time, user_id, total)
VALUES
    (1, '2024-04-10 10:30:00', 1, 50.00),   -- Ordine 1: creato il 10 aprile 2024 alle 10:30:00, utente con ID 1, totale di 50.00
    (2, '2024-04-09 15:45:00', 2, 75.25),   -- Ordine 2: creato il 9 aprile 2024 alle 15:45:00, utente con ID 2, totale di 75.25
    (3, '2024-04-08 09:00:00', 2, 100.50);  -- Ordine 3: creato il 8 aprile 2024 alle 09:00:00, utente con ID 3, totale di 100.50

INSERT INTO order_details (id, card, price, quantity, purchase, subtotal)
VALUES
    (1, 1, 20.50, 2, 1, 41.00),   -- Dettaglio dell'ordine 1: due carte con ID 1 a 20.50 ciascuna
    (2, 2, 15.75, 1, 1, 15.75),   -- Dettaglio dell'ordine 1: una carta con ID 2 a 15.75
    (3, 3, 30.00, 3, 2, 90.00);   -- Dettaglio dell'ordine 2: tre carte con ID 3 a 30.00 ciascuna


INSERT INTO users (id, first_name, last_name, email, address, phone)
VALUES
    (3, 'John', 'Djoe', 'john@gmail.com', '123 Main St, Anytown', '123-456-7890'),
    (2, 'Jane', 'Smith', 'jane@gmail.com', '456 Oak St, Anycity', '987-654-3210');

