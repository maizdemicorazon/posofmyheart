-- public.payment_methods definition

-- Drop table

-- DROP TABLE public.payment_methods;

CREATE TABLE public.payment_methods (
	id_payment_method serial4 NOT NULL,
	"name" varchar(50) NOT NULL,
	description text NULL,
	active bool DEFAULT true NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT payment_methods_pkey PRIMARY KEY (id_payment_method)
);


-- public.product_categories definition

-- Drop table

-- DROP TABLE public.product_categories;

CREATE TABLE public.product_categories (
	id_category serial4 NOT NULL,
	"name" varchar(50) NOT NULL,
	description text NULL,
	CONSTRAINT product_categories_pkey PRIMARY KEY (id_category)
);


-- public.product_extras definition

-- Drop table

-- DROP TABLE public.product_extras;

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


-- public.sauces definition

-- Drop table

-- DROP TABLE public.sauces;

CREATE TABLE public.sauces (
	id_sauce serial4 NOT NULL,
	"name" varchar(50) NOT NULL,
	description text NULL,
	CONSTRAINT id_sauce_pkey PRIMARY KEY (id_sauce)
);


-- public.orders definition

-- Drop table

-- DROP TABLE public.orders;

CREATE TABLE public.orders (
	id_order serial4 NOT NULL,
	order_date timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	total_amount numeric(10, 2) NOT NULL,
	id_payment_method int4 NOT NULL,
	notes text NULL,
	CONSTRAINT orders_pkey PRIMARY KEY (id_order),
	CONSTRAINT orders_id_payment_method_fkey FOREIGN KEY (id_payment_method) REFERENCES public.payment_methods(id_payment_method)
);


-- public.products definition

-- Drop table

-- DROP TABLE public.products;

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


-- public.order_details definition

-- Drop table

-- DROP TABLE public.order_details;

CREATE TABLE public.order_details (
	id_order_detail serial4 NOT NULL,
	id_order int4 NOT NULL,
	id_product int4 NOT NULL,
	id_sauce int4 NOT NULL,
	id_variant int4 NOT NULL,
	CONSTRAINT order_details_pkey PRIMARY KEY (id_order_detail),
	CONSTRAINT order_details_id_order_fkey FOREIGN KEY (id_order) REFERENCES public.orders(id_order),
	CONSTRAINT order_details_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.products(id_product),
	CONSTRAINT order_details_id_sauce_fkey FOREIGN KEY (id_sauce) REFERENCES public.sauces(id_sauce),
	CONSTRAINT order_details_id_variant_fkey FOREIGN KEY (id_variant) REFERENCES public.product_variants(id_variant)
);

-- public.product_extra_detail definition

-- Drop table

-- DROP TABLE public.product_extra_detail;

CREATE TABLE public.product_extras_detail (
    id_extra int4 NOT NULL,
	id_order_detail int4 NOT NULL,
	quantity int4 NOT NULL,
	CONSTRAINT id_extra_detail_key unique (id_extra, id_order_detail),
	CONSTRAINT order_details_extra_fkey FOREIGN KEY (id_extra) REFERENCES public.product_extras(id_extra)
	CONSTRAINT extras_detail_order_details_fkey FOREIGN KEY (id_order_detail) REFERENCES public.order_details(id_order_detail)
);


-- public.product_variants definition

-- Drop table

-- DROP TABLE public.product_variants;

CREATE TABLE public.product_variants (
	id_variant serial4 NOT NULL,
	id_product int4 NOT NULL,
	"size" varchar(30) NOT NULL,
	is_default bool DEFAULT false NULL,
	sell_price numeric(10, 2) DEFAULT 0.0 NOT NULL,
	cost_price numeric(10, 2) DEFAULT 0.0 NOT NULL,
	effective_date timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT product_variants_id_product_size_key UNIQUE (id_product, size),
	CONSTRAINT product_variants_pkey PRIMARY KEY (id_variant),
	CONSTRAINT product_variants_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.products(id_product)
);