# Documentación Actualizada de Consultas SQL - POS de MyHeart

## 📌 Tabla de Contenidos

- Consultas Básicas del Menú
- Consultas de Ventas y Análisis Comercial
- Consultas de Rentabilidad y Margen
- Consultas de Órdenes y Detalles
- Consultas de Análisis Temporal
- Consultas de Productos y Categorías

## 🍽️ Consultas Básicas del Menú

### 1. Menú completo con precios y categorías

```sql
SELECT
    p.name AS producto,
    v.size AS tamaño,
    v.actual_sell_price AS precio,
    pc.name AS categoria,
    p.description AS descripción
FROM
    products p
JOIN
    product_variants v ON p.id_product = v.id_product
JOIN
    product_categories pc ON p.id_category = pc.id_category
ORDER BY
    pc.name,
    p.name,
    v.actual_sell_price;
```

### 2. Productos por categoría con variantes

```sql
SELECT
    p.name AS producto,
    p.description,
    v.size,
    v.actual_sell_price AS precio,
    (v.actual_sell_price - v.actual_cost_price) AS ganancia_unitaria
FROM
    products p
JOIN 
    product_variants v ON p.id_product = v.id_product
WHERE
    p.id_category = :categoria_id  -- Parámetro para filtrar por categoría
ORDER BY
    p.name,
    v.actual_sell_price;
```

## 📈 Consultas de Ventas y Análisis Comercial

### 3. Ventas por categoría (con porcentaje)

```sql
SELECT
    p.name AS producto,
    pv.size AS variante,
    COUNT(od.id_order_detail) AS veces_vendido,
    SUM(od.sell_price - od.production_cost) AS ganancia_total,
    ROUND(AVG((od.sell_price - od.production_cost) / od.sell_price * 100), 2) AS margen_porcentual
FROM
    products p
JOIN
    product_variants pv ON p.id_product = pv.id_product
LEFT JOIN
    order_details od ON p.id_product = od.id_product AND pv.id_variant = od.id_variant
left join orders o on o.id_order = od.id_order
WHERE
    o.created_at >= CURRENT_DATE - INTERVAL '3 months' OR
    od.id_order_detail IS NULL
GROUP BY
    p.name, pv.size
HAVING
    COUNT(od.id_order_detail) < 5 OR
    COUNT(od.id_order_detail) IS NULL
ORDER BY
    veces_vendido ASC;
```

### 4. Productos más vendidos (top 10 con margen)

```sql
SELECT
    p.name AS producto,
    v.size AS variante,
    COUNT(*) AS unidades_vendidas,
    SUM(v.actual_sell_price) AS ingresos,
    SUM(v.actual_sell_price - v.actual_cost_price) AS ganancia,
    ROUND(AVG((v.actual_sell_price - v.actual_cost_price) / v.actual_sell_price * 100), 2) AS margen_porcentual
FROM
    order_details od
JOIN 
    products p ON od.id_product = p.id_product
JOIN 
    product_variants v ON od.id_variant = v.id_variant
GROUP BY
    p.name, v.size
ORDER BY
    unidades_vendidas DESC
LIMIT 10;
```

### 5. Ticket promedio por método de pago

```sql
SELECT
    pm.name AS metodo_pago,
    COUNT(*) AS transacciones,
    ROUND(AVG(o.total_amount), 2) AS ticket_promedio,
    SUM(o.total_amount) AS venta_total
FROM
    dailyEarnings o
JOIN 
    payment_methods pm ON o.id_payment_method = pm.id_payment_method
WHERE
    o.created_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY
    pm.name
ORDER BY
    transacciones DESC;
```

## 💰 Consultas de Rentabilidad y Margen

### 6. Margen de ganancia por producto

```sql
SELECT
    p.name AS producto,
    v.size AS variante,
    v.actual_sell_price AS precio_venta,
    v.actual_cost_price AS costo,
    (v.actual_sell_price - v.actual_cost_price) AS ganancia_unitaria,
    ROUND(((v.actual_sell_price - v.actual_cost_price) / v.actual_cost_price * 100), 2) AS margen_porcentaje,
    pc.name AS categoria
FROM
    products p
JOIN 
    product_variants v ON p.id_product = v.id_product
JOIN 
    product_categories pc ON p.id_category = pc.id_category
WHERE
    v.effective_date <= CURRENT_DATE
ORDER BY
    margen_porcentaje DESC;
```

### 7. Ganancias por período (diario, semanal, mensual)

```sql
-- Ganancias diarias
SELECT
    DATE(o.created_at) AS fecha,
    COUNT(*) AS ordenes,
    SUM(o.total_amount) AS ventas_totales,
    SUM(od.sell_price - od.production_cost) AS ganancia_productos,
    COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0) AS ganancia_extras,
    SUM(od.sell_price - od.production_cost) + COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0) AS ganancia_total
FROM
    dailyEarnings o
JOIN
    order_details od ON o.id_order = od.id_order
LEFT JOIN
    order_extras_detail oed ON od.id_order_detail = oed.id_order_detail
WHERE
    o.created_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY
    DATE(o.created_at)
ORDER BY
    fecha DESC;

-- Ganancias semanales
SELECT
    DATE_TRUNC('week', o.created_at) AS semana,
    COUNT(*) AS ordenes,
    SUM(o.total_amount) AS ventas_totales,
    SUM(od.sell_price - od.production_cost) AS ganancia_productos,
    COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0) AS ganancia_extras
FROM
    dailyEarnings o
JOIN
    order_details od ON o.id_order = od.id_order
LEFT JOIN
    order_extras_detail oed ON od.id_order_detail = oed.id_order_detail
GROUP BY
    DATE_TRUNC('week', o.created_at)
ORDER BY
    semana DESC;
```

## 🕒 Consultas de Análisis Temporal

### 8. Ventas por hora (horas pico)

```sql
SELECT
    EXTRACT(HOUR FROM o.created_at) AS hora,
    COUNT(*) AS ordenes,
    SUM(o.total_amount) AS ventas_totales,
    ROUND(AVG(o.total_amount), 2) AS ticket_promedio
FROM
    dailyEarnings o
WHERE
    o.created_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY
    EXTRACT(HOUR FROM o.created_at)
ORDER BY
    ordenes DESC;
```

### 9. Comparativo ventas día actual vs día anterior

```sql
SELECT
    'Hoy' AS periodo,
    COUNT(*) AS ordenes,
    SUM(total_amount) AS ventas_totales
FROM
    dailyEarnings
WHERE
    DATE(created_at) = CURRENT_DATE
UNION ALL
SELECT
    'Ayer' AS periodo,
    COUNT(*) AS ordenes,
    SUM(total_amount) AS ventas_totales
FROM
    dailyEarnings
WHERE
    DATE(created_at) = CURRENT_DATE - INTERVAL '1 day'
UNION ALL
SELECT
    'Misma día semana pasada' AS periodo,
    COUNT(*) AS ordenes,
    SUM(total_amount) AS ventas_totales
FROM
    dailyEarnings
WHERE
    DATE(created_at) = CURRENT_DATE - INTERVAL '7 days';
```

## 🛒 Consultas de Órdenes y Detalles

### 10. Detalle completo de una orden

```sql
SELECT
    o.id_order,
    o.created_at,
    pm.name AS metodo_pago,
    o.total_amount,
    o.client_name,
    p.name AS producto,
    pv.size AS variante,
    od.sell_price AS precio_unitario,
    (SELECT STRING_AGG(ps.name, ', ') 
     FROM order_detail_sauce ods 
     JOIN product_sauces ps ON ods.id_sauce = ps.id_sauce
     WHERE ods.id_order_detail = od.id_order_detail) AS salsas,
    (SELECT STRING_AGG(CONCAT(pe.name, ' (x', oed.quantity, ')'), ', ') 
     FROM order_extras_detail oed 
     JOIN product_extras pe ON oed.id_extra = pe.id_extra
     WHERE oed.id_order_detail = od.id_order_detail) AS extras,
    (od.sell_price - od.production_cost) AS ganancia_item
FROM
    dailyEarnings o
JOIN 
    payment_methods pm ON o.id_payment_method = pm.id_payment_method
JOIN 
    order_details od ON o.id_order = od.id_order
JOIN 
    products p ON od.id_product = p.id_product
JOIN 
    product_variants pv ON od.id_variant = pv.id_variant
WHERE
    o.id_order = :orden_id  -- Parámetro para filtrar por orden
ORDER BY
    p.name;
```

### 11. Ganancias por pedido (incluyendo extras)

```sql
SELECT 
    o.id_order,
    o.created_at,
    o.total_amount AS total_venta,
    SUM(od.sell_price) AS subtotal_productos,
    SUM(od.production_cost) AS costo_productos,
    SUM(od.sell_price - od.production_cost) AS ganancia_productos,
    COALESCE(SUM(oed.quantity * oed.sell_price), 0) AS subtotal_extras,
    COALESCE(SUM(oed.quantity * oed.production_cost), 0) AS costo_extras,
    COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0) AS ganancia_extras,
    (SUM(od.sell_price - od.production_cost) + 
     COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0)) AS ganancia_total,
    ROUND((SUM(od.sell_price - od.production_cost) + 
          COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0)) / 
          o.total_amount * 100, 2) AS margen_porcentual
FROM 
    dailyEarnings o
JOIN 
    order_details od ON o.id_order = od.id_order
LEFT JOIN 
    order_extras_detail oed ON od.id_order_detail = oed.id_order_detail
GROUP BY 
    o.id_order, o.created_at, o.total_amount
ORDER BY 
    o.created_at DESC
LIMIT 50;
```

## 📊 Consultas de Análisis de Productos

### 12. Análisis completo por producto

```sql
SELECT
    p.id_product,
    p.name AS producto,
    pv.size AS variante,
    pc.name AS categoria,
    COUNT(od.id_order_detail) AS veces_vendido,
    SUM(od.sell_price) AS ventas_producto,
    SUM(od.production_cost) AS costo_producto,
    SUM(od.sell_price - od.production_cost) AS ganancia_producto,
    COUNT(DISTINCT oed.id_extra) AS extras_diferentes,
    COALESCE(SUM(oed.quantity), 0) AS cantidad_extras,
    COALESCE(SUM(oed.quantity * oed.sell_price), 0) AS ventas_extras,
    COALESCE(SUM(oed.quantity * oed.production_cost), 0) AS costo_extras,
    COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0) AS ganancia_extras,
    (SUM(od.sell_price) + COALESCE(SUM(oed.quantity * oed.sell_price), 0)) AS ventas_totales,
    (SUM(od.production_cost) + COALESCE(SUM(oed.quantity * oed.production_cost), 0)) AS costos_totales,
    (SUM(od.sell_price - od.production_cost) + 
     COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0)) AS ganancia_total,
    ROUND((SUM(od.sell_price - od.production_cost) + 
          COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0)) / 
          NULLIF((SUM(od.sell_price) + COALESCE(SUM(oed.quantity * oed.sell_price), 0)), 0) * 100, 2) AS margen_porcentual
FROM
    products p
JOIN
    product_variants pv ON p.id_product = pv.id_product
JOIN
    product_categories pc ON p.id_category = pc.id_category
LEFT JOIN
    order_details od ON p.id_product = od.id_product AND pv.id_variant = od.id_variant
LEFT JOIN
    order_extras_detail oed ON od.id_order_detail = oed.id_order_detail
GROUP BY
    p.id_product, p.name, pv.size, pc.name
ORDER BY
    ganancia_total DESC;

```

### 13. Productos con bajo rendimiento

```sql
SELECT
    p.name AS producto,
    pv.size AS variante,
    COUNT(od.id_order_detail) AS veces_vendido,
    SUM(od.sell_price - od.production_cost) AS ganancia_total,
    ROUND(AVG((od.sell_price - od.production_cost) / od.sell_price * 100), 2) AS margen_porcentual
FROM
    products p
JOIN
    product_variants pv ON p.id_product = pv.id_product
LEFT JOIN
    order_details od ON p.id_product = od.id_product AND pv.id_variant = od.id_variant
WHERE
    od.created_at >= CURRENT_DATE - INTERVAL '3 months' OR
    od.id_order_detail IS NULL
GROUP BY
    p.name, pv.size
HAVING
    COUNT(od.id_order_detail) < 5 OR
    COUNT(od.id_order_detail) IS NULL
ORDER BY
    veces_vendido ASC;
```
