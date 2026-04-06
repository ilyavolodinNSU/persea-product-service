CREATE INDEX products_bm25_idx ON products
USING bm25 (name)
WITH (key_field='id');