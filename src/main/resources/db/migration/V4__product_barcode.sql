alter table products
    add column if not exists barcode varchar(255);

create unique index if not exists products_barcode_uidx
    on products (barcode)
    where barcode is not null;
