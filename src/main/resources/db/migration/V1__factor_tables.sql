-- абстрактные факторы и их нормы привязаны только к категориям  
create table if not exists categories (
    id bigserial primary key,
    name varchar(255) not null unique,
    code varchar(255) not null unique -- для бизнес логики
);

-- только для числовых типов
create table if not exists units (
    id smallserial primary key,
    name varchar(50) not null unique
);

-- числовой, булевый
create table if not exists factor_types (
    id smallserial primary key,
    name varchar(50) not null unique
);
-- список всех существующих абстрактных факторов в системе
-- например: сахар, нитраты, тип упаковки
create table if not exists factors (
    id bigserial primary key,
    name varchar(255) not null unique,
    factor_type_id smallint not null references factor_types(id) on delete cascade,
    description text
);

-- если factor_type = num
create table if not exists factor_numeric_rules (
    id bigserial primary key,
    factor_id bigint not null references factors(id) on delete cascade,
    category_id bigint not null references categories(id) on delete cascade,
    unit_id smallint references units(id),
    min_value numeric(15,10) not null,
    max_value numeric(15,10) not null,
    -- штраф расчитывается бизнес логикой
    unique (factor_id, category_id)
);

-- если factor_type = bool
create table if not exists factor_boolean_rules (
    id bigserial primary key,
    factor_id bigint not null references factors(id) on delete cascade,
    category_id bigint not null references categories(id) on delete cascade,
    impact int not null default 0, -- штраф/бонус
    unique (factor_id, category_id)
);

-- словарь
create table if not exists factor_enum_values (
    id bigserial primary key,
    factor_id bigint not null references factors(id) on delete cascade,
    value varchar(255) not null,
    unique (factor_id, value)
);

-- если factor_type = enum
create table if not exists factor_enum_rules (
    id bigserial primary key,
    category_id bigint not null references categories(id) on delete cascade,
    enum_value_id bigint not null references factor_enum_values(id) on delete cascade,
    impact int not null default 0, -- штраф/бонус
    unique (category_id, enum_value_id)
);