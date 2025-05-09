DROP TABLE products;

CREATE TABLE products (
    id_product SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    sell_price NUMERIC(10,2) NOT NULL,
    real_price NUMERIC(10,2) NOT NULL
);

-- Crear un índice explícito para id_product (aunque la clave primaria ya genera uno por defecto)
CREATE INDEX idx_products_id_product ON products(id_product);


INSERT INTO products (name, description, sell_price, real_price) VALUES
('Esquites Tradicionales chicos', '8 onzas de esquites con mayonesa, queso y picante', 30.00, 0.0),
('Esquites Tradicionales medianos', '10 onzas de esquites con mayonesa, queso y picante', 40.00, 0.0),
('Esquites Tradicionales grandes', '12 onzas de esquites con mayonesa, queso y picante', 50.00, 0.0),
('Esquites Tradicionales extra grandes', '14 onzas de esquites con mayonesa, queso y picante', 60.00, 0.0),
('Esquites con Queso Manchego ó Amarillo chicos', '8 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo', 35.00, 0.0),
('Esquites con Queso Manchego ó Amarillo medianos', '10 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo', 45.00, 0.0),
('Esquites con Queso Manchego ó Amarillo grandes', '12 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo', 55.00, 0.0),
('Esquites con Queso Manchego ó Amarillo extra grandes', '14 onzas de esquites tradicionales con una rebanada de queso manchego o 50 ml de queso amarillo', 65.00, 0.0),
('Esquite Maruchan', 'Sopa maruchan con 8 onzas esquites', 70.00, 0.0),
('Tostiesquites / Doriesquites', 'Bolsa de botana con 8 onzas esquites', 55.00, 0.0),
('Maíz Puerco', 'Maruchan, 12 oz de esquites, falda y costilla de puerco', 120.00, 0.0),
('Maíz Puereo sin sopa maruchan', '12 oz de esquites, falda y costilla de puerco', 100.00, 0.0),
('Elote Tradicional', 'Elote con mayonesa, queso y picante', 30.00, 0.0),
('Elote Revolcado', 'Topping a Elegir', 45.00, 0.0),
('Queso Manchego Extra', 'Rebanada de queso extra', 7.00, 0.0),
('Queso Amarillo Extra', '50 ml Queso amarillo líquido extra', 4.00, 0.0),
('Costilla extra', '200 gramos de costilla extra', 20.00, 0.0),
('Porción de Falda Extra', '200 gramos de carne deshebrada extra', 22.00, 0.0),
('Coca 400', 'Refresco en pet 400 ml', 20.00, 13.42),
('Coca 600', 'Refresco en pet 600 ml', 25.00, 17.92),
('Coca Lata', 'Refresco en lata 355 ml', 25.00, 17.92),
('Red Cola 600', 'Refresco en pet 600 ml', 20.00, 12.31),
('Boing 1/2', 'Jugo en caja 500 ml', 20.00, 12.55),
('Agua del Día 500 ml', 'Vaso de agua natural 500 ml', 25.00, 15.00),
('Agua del Día 1000 ml', 'Vaso de agua natural 1000 ml', 45.00, 25.00),
('Jumex lata', 'Jugo en lata 355 ml', 20.00, 11.00);

 commit;