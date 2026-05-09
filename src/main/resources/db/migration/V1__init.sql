-- абстрактные факторы и их нормы привязаны только к категориям  
create table categories (
    id bigserial primary key,
    name varchar(255) not null unique
);

-- только для числовых типов
create table units (
    id smallserial primary key,
    name varchar(50) not null unique
);

-- числовой, булевый
create table factor_types (
    id smallserial primary key,
    name varchar(50) not null unique
);
-- список всех существующих абстрактных факторов в системе
-- например: сахар, нитраты, тип упаковки
create table factors (
    id bigserial primary key,
    name varchar(255) not null unique,
    factor_type_id smallint not null references factor_types(id) on delete cascade,
    description text
);

-- если factor_type = num
create table factor_numeric_rules (
    id bigserial primary key,
    factor_id bigint not null references factors(id) on delete cascade,
    category_id bigint not null references categories(id) on delete cascade,
    unit_id smallint references units(id),
    range numrange not null,
    -- штраф расчитывается бизнес логикой
    unique (factor_id, category_id)
);

-- если factor_type = bool
create table factor_boolean_rules (
    id bigserial primary key,
    factor_id bigint not null references factors(id) on delete cascade,
    category_id bigint not null references categories(id) on delete cascade,
    impact int not null default 0, -- штраф/бонус
    unique (factor_id, category_id)
);

-- словарь
create table factor_enum_values (
    id bigserial primary key,
    factor_id bigint not null references factors(id) on delete cascade,
    value varchar(255) not null,
    unique (factor_id, value)
);

-- если factor_type = enum
create table factor_enum_rules (
    id bigserial primary key,
    factor_id bigint not null references factors(id) on delete cascade,
    category_id bigint not null references categories(id) on delete cascade,
    enum_value_id bigint not null references factor_enum_values(id) on delete cascade,
    impact int not null default 0, -- штраф/бонус
    unique (factor_id, category_id, enum_value_id)
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
    rating int check (rating between 0 and 100), -- кеш вычислений
    image_uri text not null,
    updated_at timestamp not null default now() -- для логстеша
);


create table product_numeric_factors (
    id bigserial primary key,
    product_id bigint references products(id) on delete cascade,
    factor_id bigint references factors(id),
    amount numeric(12,6),
    unique (product_id, factor_id)
);

create table product_boolean_factors (
    id bigserial primary key,
    product_id bigint references products(id) on delete cascade,
    factor_id bigint references factors(id),
    value boolean,
    unique (product_id, factor_id)
);

create table product_enum_factors (
    id bigserial primary key,
    product_id bigint references products(id) on delete cascade,
    factor_id bigint references factors(id),
    enum_value_id bigint references factor_enum_values(id),
    unique (product_id, factor_id)
);