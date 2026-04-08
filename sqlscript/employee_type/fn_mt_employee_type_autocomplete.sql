CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_autocomplete(
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
        et.id,
        et.employee_type,
        et.employee_type
    FROM mt_employee_type et
    WHERE et.delete_flag = false
      AND et.active = true
      AND et.employee_type ILIKE '%' || searchText || '%'
    ORDER BY et.employee_type;
END;
$BODY$;
