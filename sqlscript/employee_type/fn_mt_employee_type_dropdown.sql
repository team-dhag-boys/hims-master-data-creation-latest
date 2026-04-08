CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_dropdown(
)
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        et.id,
        et.id,
        et.employee_type::text
    FROM mt_employee_type et
    WHERE et.delete_flag = false
      AND et.active = true
    ORDER BY et.employee_type;
END;
$BODY$;
