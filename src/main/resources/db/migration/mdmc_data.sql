-- 1. Insertar categorías de productos
INSERT INTO public.product_categories (name, description) VALUES 
('Esquites', 'Variedad de esquites tradicionales y especiales'),
('Elotes', 'Elotes preparados de diferentes formas'),
('Extras', 'Ingredientes adicionales para tus esquites o elotes'),
('Bebidas', 'Refrescos, aguas y jugos');

-- 2. Insertar métodos de pago
INSERT INTO public.payment_methods (name, description) VALUES 
('Efectivo', 'Pago en efectivo'),
('Tarjeta de crédito', 'Pago con tarjeta de crédito'),
('Tarjeta de débito', 'Pago con tarjeta de débito'),
('Transferencia', 'Pago por transferencia bancaria'),
('Mercado Pago', 'Pago a través de Mercado Pago');

-- 3. Insertar salsas
INSERT INTO public.sauces (name, description) VALUES 
('Tradicional', 'Mayonesa, queso cotija y chile piquín'),
('Valentina', 'Salsa Valentina clásica'),
('Habanero', 'Salsa picante de habanero'),
('Chipotle', 'Salsa ahumada de chipotle'),
('Sin picante', 'Sin salsa picante'),
('Mixta', 'Combinación de salsas al gusto');

-- 4. Insertar extras
INSERT INTO public.product_extras (name, description, price, cost) VALUES 
('Queso Manchego Extra', 'Rebanada de queso extra', 7.00, 3.50),
('Queso Amarillo Extra', '50 ml Queso amarillo liquido extra', 4.00, 2.00),
('Costilla extra', '200g de costilla de puerco extra', 20.00, 10.00),
('Porción de Falda Extra', '200g de falda de res deshebrada extra', 22.00, 11.00),
('Extra Maruchan', 'Sopa Maruchan adicional', 15.00, 7.50),
('Tostitos Extra', 'Bolsa de tostitos adicional', 10.00, 5.00);

-- 5. Insertar productos principales
INSERT INTO public.products (id_category, name, description) VALUES 
-- Esquites (Categoría 1)
(1, 'Esquites Tradicionales chicos', '8 onzas de esquites con mayonesa, queso y picante'),
(1, 'Esquites Tradicionales medianos', '10 onzas de esquites con mayonesa, queso y picante'),
(1, 'Esquites Tradicionales grandes', '12 onzas de esquites con mayonesa, queso y picante'),
(1, 'Esquites Tradicionales extra grandes', '14 onzas de esquites con mayonesa, queso y picante'),
(1, 'Esquites con Queso Manchego ó Amarillo chicos', '8 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo'),
(1, 'Esquites con Queso Manchego ó Amarillo medianos', '10 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo'),
(1, 'Esquites con Queso Manchego ó Amarillo grandes', '12 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo'),
(1, 'Esquites con Queso Manchego ó Amarillo extra grandes', '14 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo'),
(1, 'Esquite Maruchan', 'Sopa maruchan con 8 onzas esquites'),
(1, 'Tostiesquites / Doriesquites', 'Bolsa de botana con 8 onzas esquites'),
(1, 'Maíz Puerco', 'Maruchan, 12 oz de esquites, falda y costilla de puerco'),
(1, 'Maíz Puerco sin sopa maruchan', '12 oz de esquites, falda y costilla de puerco'),

-- Elotes (Categoría 2)
(2, 'Elote Tradicional', 'Elote con mayonesa, queso y picante'),
(2, 'Elote Revolcado', 'Elote con topping a elegir'),

-- Bebidas (Categoría 4)
(4, 'Coca 400', 'Refresco en pet 400 ml'),
(4, 'Coca 600', 'Refresco en pet 600 ml'),
(4, 'Coca Lata', 'Refresco en lata 355 ml'),
(4, 'Red Cola 600', 'Refresco en pet 600 ml'),
(4, 'Boing 1/2', 'Jugo en caja 500 ml'),
(4, 'Agua del Día 500ml', 'Vaso de agua natural 500 ml'),
(4, 'Agua del Día 1L', 'Vaso de agua natural 1000 ml'),
(4, 'Jumex lata', 'Jugo en lata 355 ml');

-- 6. Insertar variantes de productos
INSERT INTO public.product_variants (id_product, size, is_default) VALUES 
-- Esquites Tradicionales
(1, 'Chico', true), (2, 'Mediano', true), (3, 'Grande', true), (4, 'Extra Grande', true),
-- Esquites con Queso
(5, 'Chico', true), (6, 'Mediano', true), (7, 'Grande', true), (8, 'Extra Grande', true),
-- Otros esquites
(9, 'Único', true), (10, 'Único', true), (11, 'Único', true), (12, 'Único', true),
-- Elotes
(13, 'Único', true), (14, 'Único', true),
-- Bebidas
(15, '400ml', true), (16, '600ml', true), (17, '355ml', true), (18, '600ml', true),
(19, '500ml', true), (20, '500ml', true), (21, '1L', true), (22, '355ml', true);

-- 7. Insertar precios de productos
INSERT INTO public.product_prices (id_variant, sell_price, cost_price) VALUES 
-- Esquites Tradicionales
(1, 30.00, 15.00), (2, 40.00, 20.00), (3, 50.00, 25.00), (4, 60.00, 30.00),
-- Esquites con Queso
(5, 35.00, 17.50), (6, 45.00, 22.50), (7, 55.00, 27.50), (8, 65.00, 32.50),
-- Otros esquites
(9, 70.00, 35.00), (10, 55.00, 27.50), (11, 120.00, 60.00), (12, 100.00, 50.00),
-- Elotes
(13, 30.00, 15.00), (14, 45.00, 22.50),
-- Bebidas
(15, 20.00, 13.42), (16, 25.00, 17.92), (17, 25.00, 17.92), (18, 20.00, 12.31),
(19, 20.00, 12.55), (20, 25.00, 15.00), (21, 45.00, 25.00), (22, 11.00, 20.00);

-- 8. Insertar órdenes de ejemplo
INSERT INTO public.orders (order_date, total_amount, id_payment_method, notes) VALUES 
(CURRENT_TIMESTAMP - INTERVAL '2 days', 185.00, 1, 'Cliente frecuente - preferencia salsa chipotle'),
(CURRENT_TIMESTAMP - INTERVAL '1 day', 230.00, 2, 'Para llevar - pedido por teléfono'),
(CURRENT_TIMESTAMP, 150.00, 1, 'Comer en local - solicita poca mayonesa');

-- 9. Insertar detalles de órdenes
INSERT INTO public.order_details (id_order, id_product, id_sauce, id_extra, quantity, unit_price, unit_cost) VALUES 
-- Orden 1
(1, 3, 4, NULL, 1, 50.00, 25.00),  -- Esquite Grande con salsa chipotle
(1, 13, 1, 1, 2, 37.00, 18.50),    -- 2 Elotes tradicionales con queso extra
(1, 16, 5, NULL, 1, 25.00, 17.92), -- Coca 600ml sin picante

-- Orden 2
(2, 11, 1, 3, 1, 140.00, 70.00),   -- Maíz Puerco con costilla extra
(2, 19, 5, NULL, 2, 20.00, 12.55), -- 2 Boing 1/2
(2, 10, 2, NULL, 1, 55.00, 27.50), -- Tostiesquites con salsa valentina

-- Orden 3
(3, 7, 1, NULL, 1, 55.00, 27.50),  -- Esquite con Queso Grande
(3, 14, 6, NULL, 1, 45.00, 22.50), -- Elote Revolcado con salsa mixta
(3, 20, 5, NULL, 1, 25.00, 15.00), -- Agua 500ml
(3, 1, 1, NULL, 1, 30.00, 15.00);  -- Esquite Tradicional Chico