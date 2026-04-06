create or replace function autocomplete(
    p_query text, 
    p_limit int)
returns setof text as $$
    select name
    from products p
    where p.name &&& p_query::pdb.fuzzy(1, t)
    order by pdb.score(id) desc
    limit p_limit
$$ language sql;

create or replace function search(
    p_query text,
    p_category_id int,
    p_brands_ids int[],
    p_min_rating int,
    p_max_rating int,
    p_limit int,
    p_offset int
)
returns setof products as $$
    select p.*
    from products p
    where
        (
            p_query is null
            or p_query = ''
            or p.name ||| p_query::pdb.fuzzy(2)
        )
      and (p_category_id is null or p.category_id = p_category_id)
      and (p_brands_ids is null or p.brand_id = any(p_brands_ids))
      and (p_min_rating is null or p.rating >= p_min_rating)
      and (p_max_rating is null or p.rating <= p_max_rating)
    order by
        case 
            when p_query is null or p_query = '' then p.id
            else pdb.score(p.id)
        end desc
    limit p_limit
    offset p_offset;
$$ language sql;
