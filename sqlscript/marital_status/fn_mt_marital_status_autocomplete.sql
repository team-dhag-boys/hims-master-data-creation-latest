CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_autocomplete(
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
        ms.id,
        ms.marital_status_name,
        ms.marital_status_code
    FROM mt_marital_status ms
    WHERE ms.delete_flag = false
      AND ms.active = true
      AND (
            ms.marital_status_code ILIKE '%' || searchText || '%'
         OR ms.marital_status_name ILIKE '%' || searchText || '%'
      )
    ORDER BY ms.marital_status_name;
END;
$BODY$;
