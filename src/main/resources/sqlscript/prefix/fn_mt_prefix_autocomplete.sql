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