CREATE OR REPLACE FUNCTION public.fn_mt_prefix_dropdown(
	)
    RETURNS TABLE(id bigint, value bigint, label text, genderid bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
begin
return query
select
    mp.id as id,
    mp.id as value,
    mp.prefix_name::text as label,
    mp.gender_prefix as genderid
from mt_prefix mp
where mp.delete_flag = false
  and mp.active = true;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_prefix_get_by_id(
	_prefixid bigint)
    RETURNS TABLE(prefixid bigint, prefixcode character varying, prefixname character varying, genderid bigint, gendername character varying, active boolean, deleteflag boolean, createddate text, lastmodifieddate text)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        p.id,
        p.prefix_code,
        p.prefix_name,
        g.id,
        g.gender_name,
        p.active,
        p.delete_flag,
        to_char(p.created_date, 'DD-MM-YYYY HH24:MI:SS'),
 		to_char(p.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
 FROM mt_prefix p
    LEFT JOIN mt_gender g
        ON g.id = p.gender_prefix
    WHERE p.id = _prefixId
      AND p.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_prefix_autocomplete(
	searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        p.id,
        p.prefix_code,
        p.prefix_name
    FROM mt_prefix p
    WHERE p.delete_flag = false
      AND p.active = true
      AND (
            p.prefix_code ILIKE '%' || searchText || '%'
         OR p.prefix_name ILIKE '%' || searchText || '%'
      )
    ORDER BY p.prefix_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_prefix_listing(
	page integer,
	size integer,
	searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(prefixid bigint, prefixcode character varying, prefixname character varying, gendername character varying, active boolean, createddate text)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        p.id,
        p.prefix_code,
        p.prefix_name,
        g.gender_name,
        p.active,
        to_char(p.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_prefix p
    LEFT JOIN mt_gender g
        ON g.id = p.gender_prefix
    WHERE p.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(p.prefix_code) LIKE LOWER(searchText) || '%'
         OR LOWER(p.prefix_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY p.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_prefix_listing_count(
	searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        count(*)
    FROM mt_prefix p
    LEFT JOIN mt_gender g
        ON g.id = p.gender_prefix
    WHERE p.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(p.prefix_code) LIKE LOWER(searchText) || '%'
         OR LOWER(p.prefix_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_prefix_update(
	pid bigint,
	pprefixcode character varying,
	pprefixname character varying,
	pactive boolean,
	pgenderprefix bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_prefix
    SET prefix_code = pPrefixCode,
        prefix_name = pPrefixName,
        active = pActive,
        gender_prefix = pGenderPrefix
    WHERE id = pId;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_prefix_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_prefix
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
