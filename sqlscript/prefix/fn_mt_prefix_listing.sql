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