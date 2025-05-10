
-- Nuevas VISTAS para análisis de ventas

-- Vista para ver productos con sus precios actuales
CREATE OR REPLACE VIEW current_product_prices AS
SELECT c.name as category, p.id_product, p.name as product_name, p.description, p.size,
       pp.sell_price, pp.cost_price,
       (pp.sell_price - pp.cost_price) as profit_margin,
       ((pp.sell_price - pp.cost_price) / pp.sell_price * 100) as profit_percentage
FROM products p
JOIN product_categories c ON p.id_category = c.id_category
JOIN (
    SELECT id_product, MAX(effective_date) as latest_date
    FROM product_prices
    GROUP BY id_product
) latest ON p.id_product = latest.id_product
JOIN product_prices pp ON latest.id_product = pp.id_product
    AND latest.latest_date = pp.effective_date;

-- Vista para análisis de ventas por hora del día
CREATE OR REPLACE VIEW sales_by_hour AS
SELECT
    EXTRACT(HOUR FROM order_date) as hour_of_day,
    COUNT(id_order) as total_orders,
    SUM(total_amount) as total_sales
FROM orders
GROUP BY EXTRACT(HOUR FROM order_date)
ORDER BY total_sales DESC;

-- Vista para análisis de ventas por día de la semana
CREATE OR REPLACE VIEW sales_by_weekday AS
SELECT
    TO_CHAR(order_date, 'Day') as day_of_week,
    EXTRACT(DOW FROM order_date) as day_number,
    COUNT(id_order) as total_orders,
    SUM(total_amount) as total_sales
FROM orders
GROUP BY TO_CHAR(order_date, 'Day'), EXTRACT(DOW FROM order_date)
ORDER BY day_number;

-- Vista para análisis de ventas por fecha
CREATE OR REPLACE VIEW sales_by_date AS
SELECT
    DATE(order_date) as sale_date,
    COUNT(id_order) as total_orders,
    SUM(total_amount) as total_sales
FROM orders
GROUP BY DATE(order_date)
ORDER BY sale_date DESC;

-- Vista para productos más vendidos
CREATE OR REPLACE VIEW top_selling_products AS
SELECT
    p.id_product,
    c.name as category,
    p.name as product_name,
    p.size,
    SUM(od.quantity) as total_quantity_sold,
    SUM(od.quantity * od.unit_price) as total_sales,
    SUM(od.quantity * (od.unit_price - od.unit_cost)) as total_profit
FROM order_details od
JOIN products p ON od.id_product = p.id_product
JOIN product_categories c ON p.id_category = c.id_category
GROUP BY p.id_product, c.name, p.name, p.size
ORDER BY total_quantity_sold DESC;

-- Vista para productos más vendidos por hora del día
CREATE OR REPLACE VIEW top_products_by_hour AS
SELECT
    EXTRACT(HOUR FROM o.order_date) as hour_of_day,
    p.id_product,
    p.name as product_name,
    p.size,
    SUM(od.quantity) as total_quantity_sold
FROM order_details od
JOIN orders o ON od.id_order = o.id_order
JOIN products p ON od.id_product = p.id_product
GROUP BY EXTRACT(HOUR FROM o.order_date), p.id_product, p.name, p.size
ORDER BY hour_of_day, total_quantity_sold DESC;

-- Vista para productos más vendidos por día de la semana
CREATE OR REPLACE VIEW top_products_by_weekday AS
SELECT
    TO_CHAR(o.order_date, 'Day') as day_of_week,
    EXTRACT(DOW FROM o.order_date) as day_number,
    p.id_product,
    p.name as product_name,
    p.size,
    SUM(od.quantity) as total_quantity_sold
FROM order_details od
JOIN orders o ON od.id_order = o.id_order
JOIN products p ON od.id_product = p.id_product
GROUP BY TO_CHAR(o.order_date, 'Day'), EXTRACT(DOW FROM o.order_date), p.id_product, p.name, p.size
ORDER BY day_number, total_quantity_sold DESC;