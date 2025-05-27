# Documentación de la Base de Datos - POS de MyHeart
Foobar is a Python library for dealing with word pluralization.

## 📌 Tabla de Contenidos

- Estructura de la Base de Datos

- Diagrama ER

- Tablas Principales

- Relaciones

- Inicialización de Datos

- Consultas Comunes

- Configuración

## 🗃️ Estructura de la Base de Datos

La base de datos está diseñada para un sistema POS (Point of Sale) especializado en venta de esquites, elotes y bebidas.
![mdmc_db.png](mdmc_db.png)

## 📊 Tablas Principales
1. product_categories
   Almacena categorías de productos (Esquites, Elotes, Bebidas)

Campos: id_category, name, description

2. products
   Contiene los productos principales del menú

Campos: id_product, id_category, name, description, image, created_at, updated_at

3. product_variants
   Variantes de tamaño/precio para cada producto

Campos: id_variant, id_product, size, sell_price, cost_price

4. orders
   Registro de órdenes/pedidos

Campos: id_order, order_date, total_amount, id_payment_method, notes

5. order_details
   Detalle de items en cada orden

Campos: id_order_detail, id_order, id_product, id_sauce, id_variant

🔗 Relaciones Clave
Orders → PaymentMethods: Relación muchos-a-uno (id_payment_method)

Products → ProductCategory: Relación muchos-a-uno (id_category)

OrderDetails → Orders: Relación muchos-a-uno (id_order)

ProductVariants → Products: Relación muchos-a-uno (id_product)

## 🔗 Relaciones Clave

- Orders → PaymentMethods: Relación muchos-a-uno (id_payment_method)

- Products → ProductCategory: Relación muchos-a-uno (id_category)

- OrderDetails → Orders: Relación muchos-a-uno (id_order)

- ProductVariants → Products: Relación muchos-a-uno (id_product)

## 🌱 Inicialización de Datos

- El sistema incluye un inicializador de datos que carga:

- Métodos de pago (Efectivo, Tarjetas, Transferencia)

- Categorías de productos (Esquites, Elotes, Bebidas)

- Salsas disponibles (Tradicional, Valentina, Habanero, etc.)

- Productos extras (Queso extra, Costilla extra, etc.)

- Productos principales con sus variantes

#### Ejecución: Se activa automáticamente al iniciar la aplicación en perfil dev.

## Obtener menú completo con precios:
``` sql
select
	p.name as product,
	v.size,
	v.sell_price as price,
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
	SUM(v.sell_price) as total_sales
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
	SUM(pv.sell_price) as ingresos
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
	v.sell_price as precio_venta,
	v.cost_price as costo,
	(v.sell_price - v.cost_price) as ganancia_unitaria,
	ROUND(((v.sell_price - v.cost_price) / v.cost_price * 100), 2) as margen_porcentaje,
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
	v.cost_price as costo,
	v.sell_price as precio_venta,
	ROUND((v.sell_price - v.cost_price) / v.cost_price * 100, 2) as margen_porcentaje
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
	v.sell_price as precio
from
	products p
join product_variants v on
	p.id_product = v.id_product
where
	p.id_category = 1
	-- ID de categoría (Esquites)
order by
	p.name,
	v.sell_price;
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
	pv.sell_price as precio_unitario,
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
	pv.sell_price,
	s.name,
	ped.quantity,
	pe.price;
```

## Run

``` bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## License
#### Todo los derechos reservados.