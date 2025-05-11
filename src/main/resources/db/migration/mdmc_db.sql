-- DROP SCHEMA public;

-- CREATE SCHEMA public AUTHORIZATION pg_database_owner;

-- COMMENT ON SCHEMA public IS 'standard public schema';

CREATE TABLE public.flyway_schema_history (
	installed_rank int4 NOT NULL,
	"version" varchar(50) NULL,
	description varchar(200) NULL,
	"type" varchar(20) NULL,
	script varchar(1000) NULL,
	checksum int4 NULL,
	installed_by varchar(100) NULL,
	installed_on timestamp DEFAULT now() NULL,
	execution_time int4 NULL,
	success bool NULL,
	CONSTRAINT flyway_schema_history_pkey PRIMARY KEY (installed_rank)
);

-- public.payment_methods definition

CREATE TABLE public.payment_methods (
	id_payment_method serial4 NOT NULL,
	"name" varchar(50) NOT NULL,
	description text NULL,
	active bool DEFAULT true NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT payment_methods_pkey PRIMARY KEY (id_payment_method)
);

-- public.product_categories definition
CREATE TABLE public.product_categories (
	id_category serial4 NOT NULL,
	"name" varchar(50) NOT NULL,
	description text NULL,
	CONSTRAINT product_categories_pkey PRIMARY KEY (id_category)
);

-- public.product_extras definition
CREATE TABLE public.product_extras (
	id_extra serial4 NOT NULL,
	"name" varchar(100) NOT NULL,
	description text NULL,
	price numeric(10, 2) NOT NULL,
	"cost" numeric(10, 2) NOT NULL,
	active bool DEFAULT true NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT product_extras_pkey PRIMARY KEY (id_extra)
);

-- public.orders definition
CREATE TABLE public.orders (
	id_order serial4 NOT NULL,
	order_date timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	total_amount numeric(10, 2) NOT NULL,
	id_payment_method int4 NOT NULL,
	notes text NULL,
	CONSTRAINT orders_pkey PRIMARY KEY (id_order),
	CONSTRAINT orders_id_payment_method_fkey FOREIGN KEY (id_payment_method) REFERENCES public.payment_methods(id_payment_method)
);
CREATE INDEX idx_order_date ON public.orders USING btree (order_date);

-- public.products definition
CREATE TABLE public.products (
	id_product serial4 NOT NULL,
	id_category int4 NOT NULL,
	"name" varchar(100) NOT NULL,
	description text NOT NULL,
	image varchar NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT products_pkey PRIMARY KEY (id_product),
	CONSTRAINT products_id_category_fkey FOREIGN KEY (id_category) REFERENCES public.product_categories(id_category)
);
CREATE INDEX idx_product_category ON public.products USING btree (id_category);

-- public.CREATE TABLE sauces definition
CREATE TABLE sauces (
    id_sauce serial4 NOT NULL,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    CONSTRAINT id_sauce_pkey PRIMARY KEY (id_sauce)
);
CREATE INDEX idx_sauces ON public.products USING btree (id_sauce);

-- public.order_details definition
CREATE TABLE public.order_details (
	id_order_detail serial4 NOT NULL,
	id_order int4 NOT NULL,
	id_product int4 NOT NULL,
	id_sauce int4 NOT NULL,
	id_extra int4 NULL,
	quantity int4 NOT NULL,
	unit_price numeric(10, 2) NOT NULL,
	unit_cost numeric(10, 2) NOT NULL,
	CONSTRAINT order_details_pkey PRIMARY KEY (id_order_detail),
	CONSTRAINT fk_order_details_extra_fkey FOREIGN KEY (id_extra) REFERENCES public.product_extras(id_extra),
	CONSTRAINT order_details_id_order_fkey FOREIGN KEY (id_order) REFERENCES public.orders(id_order),
	CONSTRAINT order_details_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.products(id_product),
	CONSTRAINT order_details_id_sauce_fkey FOREIGN KEY (id_sauce) REFERENCES sauces(id_sauce)
);
CREATE INDEX idx_order_details_order ON public.order_details USING btree (id_order);
CREATE INDEX idx_order_details_product ON public.order_details USING btree (id_product);
CREATE INDEX idx_order_details_sauce ON public.order_details USING btree (id_sauce);

-- public.product_variants definition
CREATE TABLE public.product_variants (
	id_variant serial4 NOT NULL,
	id_product int4 NOT NULL,
	"size" varchar(30) NOT NULL,
	is_default bool DEFAULT false NULL,
	CONSTRAINT product_variants_id_product_size_key UNIQUE (id_product, size),
	CONSTRAINT product_variants_pkey PRIMARY KEY (id_variant),
	CONSTRAINT product_variants_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.products(id_product)
);
CREATE INDEX idx_product_prices_variant_id ON public.product_variants USING btree (id_variant);

-- public.product_prices definition
CREATE TABLE public.product_prices (
	id_price serial4 NOT NULL,
	id_variant int4 NOT NULL,
	sell_price numeric(10, 2) NOT NULL,
	cost_price numeric(10, 2) NOT NULL,
	effective_date timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT product_prices_pkey PRIMARY KEY (id_price),
	CONSTRAINT product_prices_id_variant_fkey FOREIGN KEY (id_variant) REFERENCES public.product_variants(id_variant)
);