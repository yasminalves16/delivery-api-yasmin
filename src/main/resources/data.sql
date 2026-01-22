-- Inserir clientes
INSERT INTO customers (name, email, phone, address, created_at, active) VALUES
('João Silva', 'joao@email.com', '(11) 99999-1111', 'Rua A, 123 - São Paulo/SP', CURRENT_TIMESTAMP, true),
('Maria Santos', 'maria@email.com', '(11) 99999-2222', 'Rua B, 456 - São Paulo/SP', CURRENT_TIMESTAMP, true),
('Pedro Oliveira', 'pedro@email.com', '(11) 99999-3333', 'Rua C, 789 - São Paulo/SP', CURRENT_TIMESTAMP, true);

-- Inserir restaurantes
INSERT INTO restaurants (name, category, address, phone, delivery_fee, rating, active) VALUES
('Pizzaria Bella', 'Italiana', 'Av. Paulista, 1000 - São Paulo/SP', '(11) 3333-1111', 5.00, 4.5, true),
('Burger House', 'Hamburgueria', 'Rua Augusta, 500 - São Paulo/SP', '(11) 3333-2222', 3.50, 4.2, true),
('Sushi Master', 'Japonesa', 'Rua Liberdade, 200 - São Paulo/SP', '(11) 3333-3333', 8.00, 4.8, true);

-- Inserir produtos
INSERT INTO products (id, name, description, price, category, available, restaurant_id) VALUES
(1, 'Pizza Margherita', 'Molho de tomate, mussarela e manjericão', 35.90, 'Pizza', true, 1),
(2, 'Pizza Calabresa', 'Molho de tomate, mussarela e calabresa', 38.90, 'Pizza', true, 1),
(3, 'Lasanha Bolonhesa', 'Lasanha tradicional com molho bolonhesa', 28.90, 'Massa', true, 1),
(4, 'X-Burger', 'Hambúrguer, queijo, alface e tomate', 18.90, 'Hambúrguer', true, 2),
(5, 'X-Bacon', 'Hambúrguer, queijo, bacon, alface e tomate', 22.90, 'Hambúrguer', true, 2),
(6, 'Batata Frita', 'Porção de batata frita crocante', 12.90, 'Acompanhamento', true, 2),
(7, 'Combo Sashimi', '15 peças de sashimi variado', 45.90, 'Sashimi', true, 3),
(8, 'Hot Roll Salmão', '8 peças de hot roll de salmão', 32.90, 'Hot Roll', true, 3);

-- Inserir pedidos
INSERT INTO customer_orders (id, order_number, date_order, status, total_value, address_delivery, delivery_fee, customer_id, restaurant_id) VALUES
(1, 'PED1234567890', CURRENT_TIMESTAMP, 'PENDENTE', 64.80, 'Rua A, 123 - São Paulo/SP', 5.00, 1, 1),
(2, 'PED1234567891', CURRENT_TIMESTAMP, 'CONFIRMADO', 113.60, 'Rua B, 456 - São Paulo/SP', 3.50, 2, 2),
(3, 'PED1234567892', CURRENT_TIMESTAMP, 'ENTREGUE', 114.70, 'Rua C, 789 - São Paulo/SP', 8.00, 3, 3);

-- Inserir itens de pedido
INSERT INTO order_items (quantity, unit_price, subtotal, customer_order_id, product_id) VALUES
(1, 35.90, 35.90, 1, 1),
(1, 28.90, 28.90, 1, 3),
(2, 35.90, 71.80, 2, 1), 
(1, 18.90, 18.90, 2, 4),
(1, 45.90, 45.90, 3, 7),
(1, 32.90, 32.90, 3, 8),
(1, 35.90, 35.90, 3, 1);  