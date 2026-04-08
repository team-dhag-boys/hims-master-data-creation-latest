CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_autocomplete(
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
        ec.id,
        ec.category,
        ec.code
    FROM mt_employee_category ec
    WHERE ec.delete_flag = false
      AND ec.active = true
      AND (
            ec.category ILIKE '%' || searchText || '%'
         OR ec.code ILIKE '%' || searchText || '%'
      )
    ORDER BY ec.category;
END;
$BODY$;
