create table if not exists outbox (
    id uuid primary key default gen_random_uuid(),
    aggregatetype varchar(255) not null,
    aggregateid varchar(255) not null,
    type varchar(255) not null,
    payload jsonb not null,
    timestamp timestamptz default now()
);