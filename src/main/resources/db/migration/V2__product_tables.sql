create table brands (
    id bigserial primary key,
    name varchar(255) not null unique,
    description text
);

create table products (
    id bigserial primary key,
    name varchar(255) not null,
    brand_id bigint not null references brands(id),
    category_id bigint not null references categories(id),
    rating int check (rating between 0 and 100), -- кеш вычислений
    image_uri text not null,
    updated_at timestamp not null default now() -- для логстеша
);

create table product_numeric_factors (
    id bigserial primary key,
    product_id bigint not null references products(id) on delete cascade,
    factor_id bigint not null references factors(id),
    amount numeric(12,6) not null,
    unique (product_id, factor_id)
);

create table product_boolean_factors (
    id bigserial primary key,
    product_id bigint not null references products(id) on delete cascade,
    factor_id bigint not null references factors(id),
    value boolean not null,
    unique (product_id, factor_id)
);

create table product_enum_factors (
    id bigserial primary key,
    product_id bigint not null references products(id) on delete cascade,
    factor_id bigint not null references factors(id),
    enum_value_id bigint not null references factor_enum_values(id),
    unique (product_id, factor_id)
);