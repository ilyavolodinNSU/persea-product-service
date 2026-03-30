create table categories (
    id bigserial primary key,
    name varchar(255) not null unique
);

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
    rating int not null check (rating between 0 and 100), -- кеш вычислений
    image_uri text not null
);

-- g, mg, kg
create table units (
    id smallserial primary key,
    name varchar(50) not null unique
);

-- numeric, bool, enum
create table factor_types (
    id smallserial primary key,
    name varchar(50) not null unique
);

-- sugar (numeric) , packaging_type (enum)
create table health_factors (
    id bigserial primary key,
    name varchar(255) not null unique,
    factor_type_id smallint not null references factor_types(id),
    unit_id smallint references units(id),
    description text
);

-- enum type
create table factor_enum_values (
    id bigserial primary key,
    factor_id bigint not null references health_factors(id) on delete cascade,
    value varchar(255) not null,
    unique(factor_id, value)
);

--
create table product_numeric_factors (
    id bigserial primary key,
    product_id bigint references products(id) on delete cascade,
    factor_id bigint references health_factors(id),
    amount numeric(12,6),
    unique (product_id, factor_id)
);

create table product_boolean_factors (
    id bigserial primary key,
    product_id bigint references products(id) on delete cascade,
    factor_id bigint references health_factors(id),
    value boolean,
    unique (product_id, factor_id)
);

create table product_enum_factors (
    id bigserial primary key,
    product_id bigint references products(id) on delete cascade,
    factor_id bigint references health_factors(id),
    enum_value_id bigint references factor_enum_values(id),
    unique (product_id, factor_id)
);

create table factor_rules (
    id bigserial primary key,
    factor_id bigint not null references health_factors(id),
    rule_type varchar(50) not null, -- flat, range, log
    min_value numeric,
    max_value numeric,
    base_score numeric not null,
    multiplier numeric default 1,
    min_penalty numeric,
    max_penalty numeric
);

-- для разных категорий одни и те же факторы могут отличатся
create table factor_rule_categories (
    id bigserial primary key,
    rule_id bigint references factor_rules(id) on delete cascade,
    category_id bigint references categories(id) on delete cascade,
    unique (rule_id, category_id)
);