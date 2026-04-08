CREATE OR REPLACE FUNCTION public.fn_mt_nationality_autocomplete(
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
        n.id,
        n.nationality_name,
        n.nationality_code
    FROM mt_nationality n
    WHERE n.delete_flag = false
      AND n.active = true
      AND (
            n.nationality_code ILIKE '%' || searchText || '%'
         OR n.nationality_name ILIKE '%' || searchText || '%'
      )
    ORDER BY n.nationality_name;
END;
$BODY$;
