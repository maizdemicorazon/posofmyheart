-- Inserción de categorías de productos
INSERT INTO product_categories (name, description) VALUES
('Esquites', 'Diferentes presentaciones de esquites de maíz'),
('Elotes', 'Elotes preparados'),
('Bebidas', 'Refrescos y aguas'),
('Extras', 'Ingredientes y porciones adicionales');

-- Inserción de productos
-- Esquites Tradicionales
INSERT INTO products (id_category, name, description, size) VALUES
(1, 'Esquites Tradicionales', '8 onzas de esquites con mayonesa, queso y picante', 'chico'),
(1, 'Esquites Tradicionales', '10 onzas de esquites con mayonesa, queso y picante', 'mediano'),
(1, 'Esquites Tradicionales', '12 onzas de esquites con mayonesa, queso y picante', 'grande'),
(1, 'Esquites Tradicionales', '14 onzas de esquites con mayonesa, queso y picante', 'extra grande');

-- Esquites con Queso Manchego o Amarillo
INSERT INTO products (id_category, name, description, size) VALUES
(1, 'Esquites con Queso Manchego ó Amarillo', '8 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo', 'chico'),
(1, 'Esquites con Queso Manchego ó Amarillo', '10 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo', 'mediano'),
(1, 'Esquites con Queso Manchego ó Amarillo', '12 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo', 'grande'),
(1, 'Esquites con Queso Manchego ó Amarillo', '14 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo', 'extra grande');

-- Especialidades
INSERT INTO products (id_category, name, description, size) VALUES
(1, 'Esquite Maruchan', 'Sopa maruchan con 8 onzas esquites', NULL),
(1, 'Tostiesquites / Doriesquites', 'Bolsa de botana con 8 onzas esquites', NULL),
(1, 'Maíz Puerco', 'Maruchan, 12 oz de esquites, falda y costilla de puerco', NULL),
(1, 'Maíz Puerco sin sopa maruchan', '12 oz de esquites, falda y costilla de puerco', NULL);

-- Elotes
INSERT INTO products (id_category, name, description, size) VALUES
(2, 'Elote Tradicional', 'Elote con mayonesa, queso y picante', NULL),
(2, 'Elote Revolcado', 'Topping a Elegir', NULL);

-- Extras
INSERT INTO products (id_category, name, description, size) VALUES
(4, 'Queso Manchego Extra', 'Rebanada de queso extra', NULL),
(4, 'Queso Amarillo Extra', '50 ml Queso amarillo liquido extra', NULL),
(4, 'Costilla extra', '200 Trozo de costilla extra', NULL),
(4, 'Porción de Falda Extra', '200 gramos Carne desehebrada extra', NULL);

-- Bebidas
INSERT INTO products (id_category, name, description, size) VALUES
(3, 'Coca', 'Refresco en pet 400 ml', '400 ml'),
(3, 'Coca', 'Refresco en pet 600 ml', '600 ml'),
(3, 'Coca Lata', 'Refresco en lata 355 ml', '355 ml'),
(3, 'Red Cola', 'Refresco en pet 600 ml', '600 ml'),
(3, 'Boing', 'Jugo en caja 500 ml', '500 ml'),
(3, 'Agua del Día', 'Vaso de agua natural 500 ml', '500 ml'),
(3, 'Agua del Día', 'Vaso de agua natural 1000 ml', '1000 ml'),
(3, 'Jumex lata', 'Jugo en lata 355 ml', '355 ml');

-- Inserción de precios para los productos
-- Precios para Esquites Tradicionales
INSERT INTO product_prices (id_product, sell_price, cost_price) VALUES
(1, 30.00, 15.00),
(2, 40.00, 20.00),
(3, 50.00, 25.00),
(4, 60.00, 30.00);

-- Precios para Esquites con Queso Manchego o Amarillo
INSERT INTO product_prices (id_product, sell_price, cost_price) VALUES
(5, 35.00, 17.50),
(6, 45.00, 22.50),
(7, 55.00, 27.50),
(8, 65.00, 32.50);

-- Precios para especialidades
INSERT INTO product_prices (id_product, sell_price, cost_price) VALUES
(9, 70.00, 35.00),
(10, 55.00, 27.50),
(11, 120.00, 60.00),
(12, 100.00, 50.00);

-- Precios para elotes
INSERT INTO product_prices (id_product, sell_price, cost_price) VALUES
(13, 30.00, 15.00),
(14, 45.00, 22.50);

-- Precios para extras
INSERT INTO product_prices (id_product, sell_price, cost_price) VALUES
(15, 7.00, 3.50),
(16, 4.00, 2.00),
(17, 20.00, 10.00),
(18, 22.00, 11.00);

-- Precios para bebidas
INSERT INTO product_prices (id_product, sell_price, cost_price) VALUES
(19, 20.00, 13.42),
(20, 25.00, 17.92),
(21, 25.00, 17.92),
(22, 20.00, 12.31),
(23, 20.00, 12.55),
(24, 25.00, 15.00),
(25, 45.00, 25.00),
(26, 20.00, 11.00);

-- Inserción de métodos de pago (ya definida en la estructura)
-- Pero por claridad la incluimos aquí también
INSERT INTO payment_methods (name, description) VALUES
('Efectivo', 'Pago en efectivo'),
('Tarjeta', 'Pago con tarjeta de débito o crédito'),
('Transferencia', 'Pago mediante transferencia bancaria');

-- Inserción de órdenes de ejemplo (ventas)
-- Orden 1: Un pedido pequeño pagado en efectivo
INSERT INTO orders (created_at, total_amount, id_payment_method, notes)
VALUES ('2023-05-09 12:30:00', 70.00, 1, 'Cliente regular');

-- Orden 2: Un pedido mediano pagado con tarjeta
INSERT INTO orders (created_at, total_amount, id_payment_method, notes)
VALUES ('2023-05-09 13:45:00', 150.00, 2, 'Cliente nuevo, pagó con VISA');

-- Orden 3: Un pedido grande pagado con transferencia
INSERT INTO orders (created_at, total_amount, id_payment_method, notes)
VALUES ('2023-05-09 17:20:00', 245.00, 3, 'Pedido para oficina');

-- Orden 4: Un pedido en día diferente para mostrar análisis por fecha
INSERT INTO orders (created_at, total_amount, id_payment_method, notes)
VALUES ('2023-05-10 14:15:00', 120.00, 1, 'Cliente frecuente');

-- Orden 5: Un pedido en horario nocturno para mostrar análisis por hora
INSERT INTO orders (created_at, total_amount, id_payment_method, notes)
VALUES ('2023-05-10 19:30:00', 200.00, 2, 'Familia grande');

-- Detalles de las órdenes
-- Detalles de la orden 1
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(1, 1, 2, 30.00, 15.00),  -- 2 Esquites tradicionales chicos
(1, 15, 1, 7.00, 3.50),   -- 1 Queso manchego extra
(1, 21, 1, 25.00, 17.92); -- 1 Coca lata

-- Detalles de la orden 2
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(2, 3, 1, 50.00, 25.00),   -- 1 Esquites tradicionales grande
(2, 11, 1, 120.00, 60.00), -- 1 Maíz puerco
(2, 24, 1, 25.00, 15.00);  -- 1 Agua del día 500ml

-- Detalles de la orden 3
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(3, 4, 2, 60.00, 30.00),   -- 2 Esquites tradicionales extra grandes
(3, 13, 3, 30.00, 15.00),  -- 3 Elotes tradicionales
(3, 16, 2, 4.00, 2.00),    -- 2 Queso amarillo extra
(3, 20, 2, 25.00, 17.92);  -- 2 Coca 600ml

-- Detalles de la orden 4
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(4, 10, 1, 55.00, 27.50),  -- 1 Tostiesquites
(4, 14, 1, 45.00, 22.50),  -- 1 Elote revolcado
(4, 21, 1, 25.00, 17.92);  -- 1 Coca lata

-- Detalles de la orden 5
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(5, 2, 2, 40.00, 20.00),   -- 2 Esquites tradicionales medianos
(5, 7, 1, 55.00, 27.50),   -- 1 Esquites con queso manchego grande
(5, 18, 1, 22.00, 11.00),  -- 1 Porción de falda extra
(5, 23, 2, 20.00, 12.55);  -- 2 Boing 1/2

-- Inserción de más órdenes para un mejor análisis por hora/día
-- Más órdenes para el análisis de hora pico
INSERT INTO orders (created_at, total_amount, id_payment_method, notes)
VALUES
('2023-05-11 12:10:00', 85.00, 1, 'Cliente hora de comida'),
('2023-05-11 12:25:00', 92.00, 1, 'Cliente hora de comida'),
('2023-05-11 12:40:00', 110.00, 2, 'Cliente hora de comida'),
('2023-05-11 12:55:00', 75.00, 1, 'Cliente hora de comida'),
('2023-05-11 18:10:00', 145.00, 2, 'Cliente hora de cena'),
('2023-05-11 18:25:00', 155.00, 1, 'Cliente hora de cena'),
('2023-05-11 18:40:00', 180.00, 3, 'Cliente hora de cena'),
('2023-05-11 18:55:00', 165.00, 1, 'Cliente hora de cena');

-- Detalles de estas órdenes (simplificados para no hacer el script demasiado largo)
-- Orden 6
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(6, 1, 2, 30.00, 15.00),  -- 2 Esquites tradicionales chicos
(6, 21, 1, 25.00, 17.92); -- 1 Coca lata

-- Orden 7
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(7, 2, 2, 40.00, 20.00),  -- 2 Esquites tradicionales medianos
(7, 16, 3, 4.00, 2.00);   -- 3 Queso amarillo extra

-- Orden 8
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(8, 11, 1, 120.00, 60.00), -- 1 Maíz puerco
(8, 15, 1, 7.00, 3.50);    -- 1 Queso manchego extra

-- Orden 9
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(9, 13, 2, 30.00, 15.00),  -- 2 Elotes tradicionales
(9, 24, 1, 25.00, 15.00);  -- 1 Agua del día 500ml

-- Orden 10
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(10, 4, 2, 60.00, 30.00),  -- 2 Esquites tradicionales extra grandes
(10, 21, 1, 25.00, 17.92); -- 1 Coca lata

-- Orden 11
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(11, 7, 2, 55.00, 27.50),  -- 2 Esquites con queso manchego grande
(11, 17, 1, 20.00, 10.00), -- 1 Costilla extra
(11, 21, 1, 25.00, 17.92); -- 1 Coca lata

-- Orden 12
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(12, 11, 1, 120.00, 60.00), -- 1 Maíz puerco
(12, 18, 1, 22.00, 11.00),  -- 1 Porción de falda extra
(12, 19, 2, 20.00, 13.42);  -- 2 Coca 400ml

-- Orden 13
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(13, 3, 3, 50.00, 25.00),   -- 3 Esquites tradicionales grandes
(13, 24, 1, 25.00, 15.00);  -- 1 Agua del día 500ml

-- Inserción para analizar días de semana (diferentes días)
INSERT INTO orders (created_at, total_amount, id_payment_method, notes)
VALUES
('2023-05-12 15:30:00', 130.00, 1, 'Cliente viernes'),
('2023-05-13 16:45:00', 175.00, 2, 'Cliente sábado'),
('2023-05-14 14:20:00', 200.00, 1, 'Cliente domingo'),
('2023-05-15 13:15:00', 95.00, 1, 'Cliente lunes'),
('2023-05-16 17:30:00', 115.00, 3, 'Cliente martes');

-- Detalles simplificados para estas órdenes
-- Orden 14 (viernes)
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(14, 11, 1, 120.00, 60.00), -- 1 Maíz puerco
(14, 20, 1, 25.00, 17.92);  -- 1 Coca 600ml

-- Orden 15 (sábado)
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(15, 8, 2, 65.00, 32.50),   -- 2 Esquites con queso manchego extra grande
(15, 17, 1, 20.00, 10.00),  -- 1 Costilla extra
(15, 23, 1, 20.00, 12.55);  -- 1 Boing 1/2

-- Orden 16 (domingo)
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(16, 11, 1, 120.00, 60.00), -- 1 Maíz puerco
(16, 14, 2, 45.00, 22.50),  -- 2 Elote revolcado
(16, 15, 1, 7.00, 3.50);    -- 1 Queso manchego extra

-- Orden 17 (lunes)
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(17, 2, 2, 40.00, 20.00),   -- 2 Esquites tradicionales medianos
(17, 24, 1, 25.00, 15.00);  -- 1 Agua del día 500ml

-- Orden 18 (martes)
INSERT INTO order_details (id_order, id_product, quantity, unit_price, unit_cost) VALUES
(18, 10, 1, 55.00, 27.50),  -- 1 Tostiesquites
(18, 13, 2, 30.00, 15.00),  -- 2 Elotes tradicionales
(18, 23, 1, 20.00, 12.55);  -- 1 Boing 1/2

 -- QUERIES

-- Ver los productos más vendidos
SELECT * FROM top_selling_products LIMIT 5;

-- Ver qué horarios son más concurridos
SELECT * FROM sales_by_hour;

-- Ver qué días de la semana tienen más ventas
SELECT * FROM sales_by_weekday;

-- Ver qué método de pago es más utilizado
SELECT * FROM sales_by_payment_method;

-- Para ver qué productos se venden más por la tarde (entre 4-7 PM)
SELECT * FROM top_products_by_hour
WHERE hour_of_day BETWEEN 16 AND 19;

SELECT * FROM current_product_prices;

-- Ver productos por categoría
SELECT * FROM current_product_prices WHERE category = 'Esquites';

-- Buscar productos con mayor margen de ganancia
SELECT * FROM current_product_prices ORDER BY profit_percentage DESC LIMIT 10;

-- Ver todos los métodos de pago disponibles
SELECT * FROM payment_methods WHERE active = TRUE;

-- Consulta para ver productos más vendidos
 SELECT * FROM top_selling_products LIMIT 10;

-- Consulta para ver productos más vendidos en un rango de fechas
SELECT
    p.name as product_name,
    p.size,
    SUM(od.quantity) as total_quantity_sold
FROM order_details od
JOIN orders o ON od.id_order = o.id_order
JOIN products p ON od.id_product = p.id_product
WHERE o.created_at BETWEEN '2023-01-01' AND '2023-01-31'
GROUP BY p.name, p.size
ORDER BY total_quantity_sold DESC
LIMIT 10;