INSERT INTO expense_category (id, name) VALUES
(1, 'Uncategorized'),
(2, 'Food'),
(3, 'Travel'),
(4, 'Entertainment'),
(5, 'Household'),
(6, 'Utilities')
ON CONFLICT (id) DO NOTHING;

INSERT INTO expense (description, amount, category_id, date) VALUES
('Lunch at Cafe', 25.50, 1, '2025-02-20 12:30:00'),
('Gas refuel', 40.25, 2, '2025-02-19 08:15:00'),
('Movie tickets', 30.00, 3, '2025-02-18 19:00:00'),
('Groceries', 55.75, 1, '2025-02-17 17:45:00'), -- No category assigned
('Internet bill', 70.00, 5, '2025-02-16 14:00:00'),
('Dinner at Restaurant', 45.80, 1, '2025-02-15 20:00:00'), -- No category assigned
('Taxi fare', 20.50, 2, '2025-02-14 22:00:00'),
('Concert tickets', 75.00, 3, '2025-02-13 21:30:00'),
('Cleaning supplies', 15.25, 1, '2025-02-12 09:45:00'), -- No category assigned
('Electricity bill', 90.00, 5, '2025-02-11 16:00:00');
