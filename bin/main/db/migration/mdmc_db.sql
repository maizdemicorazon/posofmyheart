CREATE TABLE flyway_schema_history (
    installed_rank INT PRIMARY KEY,
    version VARCHAR(50),
    description VARCHAR(200),
    type VARCHAR(20),
    script VARCHAR(1000),
    checksum INT,
    installed_by VARCHAR(100),
    installed_on TIMESTAMP DEFAULT NOW(),
    execution_time INT,
    success BOOLEAN
);

CREATE TABLE product_categories (
    id_category SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT
);

CREATE TABLE products (
    id_product SERIAL PRIMARY KEY,
    id_category INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    size VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_category) REFERENCES product_categories(id_category)
);

CREATE TABLE product_prices (
    id_price SERIAL PRIMARY KEY,
    id_product INTEGER NOT NULL,
    sell_price NUMERIC(10,2) NOT NULL,
    cost_price NUMERIC(10,2) NOT NULL,
    effective_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_product) REFERENCES products(id_product)
);

-- Tabla para catálogo de métodos de pago
CREATE TABLE payment_methods (
    id_payment_method SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id_order SERIAL PRIMARY KEY,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount NUMERIC(10,2) NOT NULL,
    id_payment_method INTEGER NOT NULL,
    notes TEXT,
    FOREIGN KEY (id_payment_method) REFERENCES payment_methods(id_payment_method)
);

CREATE TABLE order_details (
    id_order_detail SERIAL PRIMARY KEY,
    id_order INTEGER NOT NULL,
    id_product INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    unit_cost NUMERIC(10,2) NOT NULL,
    FOREIGN KEY (id_order) REFERENCES orders(id_order),
    FOREIGN KEY (id_product) REFERENCES products(id_product)
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_product_category ON products(id_category);
CREATE INDEX idx_product_prices_product_id ON product_prices(id_product);
CREATE INDEX idx_order_date ON orders(order_date);
CREATE INDEX idx_order_details_order ON order_details(id_order);
CREATE INDEX idx_order_details_product ON order_details(id_product);
