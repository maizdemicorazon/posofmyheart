# Documentación de consultas de Base de Datos - POS de MyHeart

## Obtener menú completo con precios:
``` sql
select
	p.name as product,
	v.size,
	v.actual_sell_price as price,
	pc.name as category
from
	products p
join
   product_variants v on
	p.id_product = v.id_product
join
   product_categories pc on
	p.id_category = pc.id_category
order by
	pc.name,
	p.name;
```

## Calcular ventas por categoría:
``` sql
select
	pc.name as category,
	SUM(v.actual_sell_price) as total_sales
from
	order_details od
join
   products p on
	od.id_product = p.id_product
join
   product_categories pc on
	p.id_category = pc.id_category
join
   product_variants v on
	od.id_variant = v.id_variant
group by
	pc.name;
```
## Ventas por día (últimos 7 días)
``` sql
select
	DATE(order_date) as fecha,
	COUNT(*) as total_ordenes,
	SUM(total_amount) as venta_total
from
	orders
where
	order_date >= CURRENT_DATE - interval '7 days'
group by
	DATE(order_date)
order by
	fecha desc;
```

## Productos más vendidos (top 10)
``` sql
select
	p.name as producto,
	count(p.name) as unidades_vendidas,
	SUM(pv.actual_sell_price) as ingresos
from
	order_details od
join products p on
	od.id_product = p.id_product
join product_variants pv on
	od.id_variant = pv.id_variant
group by
	p.name
order by
	unidades_vendidas desc
limit 10;
```
## Productos con sus variantes y margen de ganancia

``` sql
select
	p.name as producto,
	v.size as tamaño,
	v.actual_sell_price as precio_venta,
	v.actual_cost_price as costo,
	(v.actual_sell_price - v.actual_cost_price) as ganancia_unitaria,
	ROUND(((v.actual_sell_price - v.actual_cost_price) / v.actual_cost_price * 100), 2) as margen_porcentaje,
	pc.name as categoria
from
	products p
join 
    product_variants v on
	p.id_product = v.id_product
join 
    product_categories pc on
	p.id_category = pc.id_category
order by
	margen_porcentaje desc;
```


## Inventario de productos (con margen de ganancia)

``` sql
select
	p.name,
	v.size,
	v.actual_cost_price as costo,
	v.actual_sell_price as precio_venta,
	ROUND((v.actual_sell_price - v.actual_cost_price) / v.actual_cost_price * 100, 2) as margen_porcentaje
from
	product_variants v
join products p on
	v.id_product = p.id_product
order by
	margen_porcentaje desc;
```
## Ticket promedio por método de pago
``` sql
select
	pm.name as metodo_pago,
	COUNT(*) as transacciones,
	AVG(o.total_amount) as ticket_promedio
from
	orders o
join payment_methods pm on
	o.id_payment_method = pm.id_payment_method
group by
	pm.name;
```

## Horas pico de ventas
``` sql
select
	extract(hour from order_date) as hora,
	COUNT(*) as ordenes
from
	orders
group by
	extract(hour from order_date)
order by
	ordenes desc;
```

## Buscar productos por categoría con sus variantes
``` sql
select
	p.name as producto,
	p.description,
	v.size,
	v.actual_sell_price as precio
from
	products p
join product_variants v on
	p.id_product = v.id_product
where
	p.id_category = 1
	-- ID de categoría (Esquites)
order by
	p.name,
	v.actual_sell_price;
```
##  Resumen diario (para pantalla principal)
``` sql
select
	COUNT(*) as ordenes_hoy,
	SUM(total_amount) as venta_hoy,
	(
	select
		SUM(total_amount)
	from
		orders
	where
		DATE(order_date) = CURRENT_DATE - interval '1 day') as venta_ayer
from
	orders
where
	DATE(order_date) = CURRENT_DATE;
```

## Distribución de ventas por categoría (para gráfico)
``` sql
select
	pc.name as categoria,
	ROUND(SUM(o.total_amount) / (select SUM(total_amount) from orders) * 100, 2) as porcentaje
from
	orders o
join order_details od on
	o.id_order = od.id_order
join products p on
	od.id_product = p.id_product
join product_categories pc on
	p.id_category = pc.id_category
group by
	pc.name;
```

## Detalle completo de la orden
``` sql
select
	o.id_order as orden_id,
	o.order_date as fecha,
	pm.name as metodo_pago,
	o.total_amount as total,
	p.name as producto,
	pv.size as variante,
	pv.actual_sell_price as precio_unitario,
	s.name as salsa,
	coalesce(ped.quantity, 0) as cantidad_extra,
	sum(ped.quantity) as cantidad_por_extra,
	coalesce(pe.price, 0) as precio_extra
from
	orders o
join 
    payment_methods pm on
	o.id_payment_method = pm.id_payment_method
join 
    order_details od on
	o.id_order = od.id_order
join 
    products p on
	od.id_product = p.id_product
join 
    product_variants pv on
	od.id_variant = pv.id_variant
join 
    sauces s on
	od.id_sauce = s.id_sauce
left join 
    order_extras_detail ped on
	od.id_order_detail = ped.id_order_detail
left join 
    product_extras pe on
	ped.id_extra = pe.id_extra
where
	o.id_order = 2
	-- reemplaza :ordenid con el id de la orden
group by
	o.id_order,
	o.order_date,
	pm.name,
	o.total_amount,
	p.name,
	pv.size,
	pv.actual_sell_price,
	s.name,
	ped.quantity,
	pe.price;
```

## Ganancias por Pedido (incluyendo extras)
``` sql
SELECT 
    o.id_order,
    o.order_date,
    o.total_amount AS total_venta,
    SUM(od.sell_price - od.production_cost) AS ganancia_items,
    COALESCE(SUM(ped.quantity * (ped.sell_price - ped.production_cost)), 0) AS ganancia_extras,
    (SUM(od.sell_price - od.production_cost) + COALESCE(SUM(ped.quantity * (ped.sell_price - ped.production_cost)), 0)) AS ganancia_total
FROM 
    orders o
JOIN 
    order_details od ON o.id_order = od.id_order
LEFT JOIN 
    product_extras_detail ped ON od.id_order_detail = ped.id_order_detail
GROUP BY 
    o.id_order, o.order_date, o.total_amount
ORDER BY 
    o.order_date DESC;
```

## Ganancias por Período (diario, mensual, anual)
``` sql
-- Ganancias diarias
SELECT 
    DATE(o.order_date) AS fecha,
    SUM(od.sell_price - od.production_cost) AS ganancia_items,
    COALESCE(SUM(ped.quantity * (ped.sell_price - ped.production_cost)), 0) AS ganancia_extras,
    (SUM(od.sell_price - od.production_cost) + COALESCE(SUM(ped.quantity * (ped.sell_price - ped.production_cost)), 0)) AS ganancia_total
FROM 
    orders o
JOIN 
    order_details od ON o.id_order = od.id_order
LEFT JOIN 
    product_extras_detail ped ON od.id_order_detail = ped.id_order_detail
GROUP BY 
    DATE(o.order_date)
ORDER BY 
    fecha DESC;

-- Ganancias mensuales
SELECT 
    DATE_TRUNC('month', o.order_date) AS mes,
    SUM(od.sell_price - od.production_cost) AS ganancia_items,
    COALESCE(SUM(ped.quantity * (ped.sell_price - ped.production_cost)), 0) AS ganancia_extras,
    (SUM(od.sell_price - od.production_cost) + COALESCE(SUM(ped.quantity * (ped.sell_price - ped.production_cost)), 0) AS ganancia_total
FROM 
    orders o
JOIN 
    order_details od ON o.id_order = od.id_order
LEFT JOIN 
    product_extras_detail ped ON od.id_order_detail = ped.id_order_detail
GROUP BY 
    DATE_TRUNC('month', o.order_date)
ORDER BY 
    mes DESC;
```

## Ganancias por Producto
``` sql
SELECT 
    p.id_product,
    p.name AS producto,
    pv.size AS variante,
    COUNT(od.id_order_detail) AS cantidad_vendida,
    ROUND(SUM(od.sell_price - od.production_cost), 2) AS ganancia_producto,
    ROUND(COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0), 2) AS ganancia_extras,
    ROUND((SUM(od.sell_price - od.production_cost) + COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0)), 2) AS ganancia_total
FROM 
    order_details od
JOIN 
    products p ON od.id_product = p.id_product
JOIN 
    product_variants pv ON od.id_variant = pv.id_variant
LEFT JOIN 
    order_extras_detail oed ON od.id_order_detail = oed.id_order_detail
GROUP BY 
    p.id_product, p.name, pv.size
ORDER BY 
    ganancia_total DESC;
```

## Ganancias por Categoría de Producto
``` sql
SELECT 
    pc.id_category,
    pc.name AS categoria,
    COUNT(od.id_order_detail) AS cantidad_vendida,
    ROUND(SUM(od.sell_price - od.production_cost), 2) AS ganancia_items,
    ROUND(COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0), 2) AS ganancia_extras,
    ROUND((SUM(od.sell_price - od.production_cost) + COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0)), 2) AS ganancia_total
FROM 
    order_details od
JOIN 
    products p ON od.id_product = p.id_product
JOIN 
    product_categories pc ON p.id_category = pc.id_category
LEFT JOIN 
    order_extras_detail oed ON od.id_order_detail = oed.id_order_detail
GROUP BY 
    pc.id_category, pc.name
ORDER BY 
    ganancia_total DESC;
```

## Margen de Ganancia Promedio
``` sql
SELECT 
    ROUND(AVG(CASE WHEN od.sell_price > 0 THEN (od.sell_price - od.production_cost) / od.sell_price * 100 END), 2) AS margen_promedio_productos,
    ROUND(AVG(CASE WHEN oed.sell_price > 0 THEN (oed.sell_price - oed.production_cost) / oed.sell_price * 100 END), 2) AS margen_promedio_extras
FROM 
    order_details od
LEFT JOIN 
    order_extras_detail oed ON od.id_order_detail = oed.id_order_detail
WHERE 
    od.sell_price IS 
```

## Consulta para Análisis por Producto
``` sql
SELECT
p.id_product,
p.name AS producto,
pv.size AS variante,
COUNT(od.id_order_detail) AS veces_vendido,
SUM(od.sell_price) AS total_ventas_producto,
SUM(od.production_cost) AS total_costo_producto,
SUM(od.sell_price - od.production_cost) AS ganancia_producto,

    -- Extras asociados a este producto
    COUNT(DISTINCT oed.id_extra) AS extras_diferentes_vendidos,
    COALESCE(SUM(oed.quantity), 0) AS cantidad_extras,
    COALESCE(SUM(oed.quantity * oed.sell_price), 0) AS ventas_extras,
    COALESCE(SUM(oed.quantity * oed.production_cost), 0) AS costo_extras,
    COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0) AS ganancia_extras,
    
    -- Total general
    (SUM(od.sell_price) + COALESCE(SUM(oed.quantity * oed.sell_price), 0)) AS ventas_totales,
    (SUM(od.production_cost) + COALESCE(SUM(oed.quantity * oed.production_cost), 0)) AS costos_totales,
    (SUM(od.sell_price - od.production_cost) + 
     COALESCE(SUM(oed.quantity * (oed.sell_price - oed.production_cost)), 0)) AS ganancia_total
FROM
order_details od
JOIN
products p ON od.id_product = p.id_product
JOIN
product_variants pv ON od.id_variant = pv.id_variant
LEFT JOIN
order_extras_detail oed ON od.id_order_detail = oed.id_order_detail
GROUP BY
p.id_product, p.name, pv.size
ORDER BY
ganancia_total DESC;
```