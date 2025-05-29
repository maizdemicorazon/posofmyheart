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

Campos: id_variant, id_product, size, actual_sell_price, actual_cost_price

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

## Run

``` bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## License
#### Todo los derechos reservados.