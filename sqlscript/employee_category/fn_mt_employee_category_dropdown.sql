CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_dropdown(
)
    RETURNS TABLE(id bigint, value bigint, label text, employeetypeid bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        ec.id,
        ec.id,
        ec.category::text,
        ec.employee_type_id
    FROM mt_employee_category ec
    WHERE ec.delete_flag = false
      AND ec.active = true
    ORDER BY ec.category;
END;
$BODY$;
